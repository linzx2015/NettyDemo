package com.kkk.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
/**
 * 作为服务端,等待接收客户端数据
 * @author linzx
 * */
public class NettyServer{
	public static void main(String[] args) throws InterruptedException{
		EventLoopGroup recDataGroup=new NioEventLoopGroup();//接收数据
		EventLoopGroup procDataGroup=new NioEventLoopGroup();//处理数据
		ServerBootstrap bootStrap=new ServerBootstrap();//服务器启动配置参数使用
		System.out.println("---------server start");
		bootStrap.group(recDataGroup, procDataGroup)
				 .channel(NioServerSocketChannel.class)		//指定nio模式
				 .option(ChannelOption.SO_BACKLOG, 1024)	//设置tcp缓冲区大小
				 .option(ChannelOption.SO_SNDBUF, 32*1024)  //设置发送缓冲大小
				 .option(ChannelOption.SO_RCVBUF, 32*1024)  //设置接收缓冲区大小
				 .option(ChannelOption.SO_KEEPALIVE, true)  //设置保持连接
				 .childHandler(new ChannelInitializer<SocketChannel>(){
					@Override
					protected void initChannel(SocketChannel sc) throws Exception{
						//客户端进行同样设置
						ByteBuf buf=Unpooled.copiedBuffer("$_".getBytes());//设置tcp特殊分隔符,解决黏包问题
						sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
						sc.pipeline().addLast(new StringDecoder());		  //设置字符串的解码
						sc.pipeline().addLast(new MyServerHandler());     //具体完成业务操作的部分
					}
				 });
		  ChannelFuture cf=bootStrap.bind(9999).sync();	//绑定端口
		  cf.channel().closeFuture().sync();		//等待关闭
		  
		  recDataGroup.shutdownGracefully();		//cf关闭完成之后,两个group也要安全关闭
		  procDataGroup.shutdownGracefully();
	}
}
