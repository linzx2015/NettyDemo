package com.kkk.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * netty客户端,主要用于向服务端发送数据
 * @author linzx
 * */
public class NettyClient{
	public static void main(String[] args) throws InterruptedException{
		EventLoopGroup sendGroup=new NioEventLoopGroup();//用于发送数据的group线程
		Bootstrap clientBootStrap=new Bootstrap();		 //客户端启动配置
		System.out.println("---------client start");
		clientBootStrap.group(sendGroup)
		.channel(NioSocketChannel.class)				 //与服务端使用不同
		.handler(new ChannelInitializer<SocketChannel>(){
			@Override
			protected void initChannel(SocketChannel sc) throws Exception{
				ByteBuf buf=Unpooled.copiedBuffer("$_".getBytes());//设置tcp特殊分隔符,解决黏包问题
				sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
				sc.pipeline().addLast(new StringDecoder());		  //设置字符串的解码
				sc.pipeline().addLast(new MyClientHandler());
			}
		});
		ChannelFuture cf=clientBootStrap.connect("127.0.0.1",9999).sync();//绑定连接本机9999
		
		Channel channel=cf.channel(); //获取通道准备往服务端发送数据
		channel.writeAndFlush(Unpooled.copiedBuffer("hi server$_".getBytes()));
		Thread.sleep(1000);
		channel.writeAndFlush(Unpooled.copiedBuffer("from client222$_".getBytes()));
		Thread.sleep(1000);
		channel.writeAndFlush(Unpooled.copiedBuffer("from333client$_".getBytes()));
		Thread.sleep(1000);
		
		channel.closeFuture().sync();//等待客户端通道关闭
		sendGroup.shutdownGracefully();
	}
}
