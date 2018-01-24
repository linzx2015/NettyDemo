package com.kkk.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringDecoder;
/***
 * 完成业务处理的类
 * @author linzx
 * */
public class MyServerHandler extends ChannelHandlerAdapter{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
//		ByteBuf buf=(ByteBuf) msg;//ByteBuf 有读和写两个指针
//		byte[] recData=new byte[buf.readableBytes()];
//		buf.readBytes(recData);
//		
//		String getMsg=new String(recData,"utf-8");
//		System.out.println("server receive data:"+getMsg);
		
		String recData=(String) msg; //在Server端设置了StringDecoder,此处可直接强转
		System.out.println("server receive:"+recData);
		
		///收到数据之后,给出相应
		String respData="send hello to client$_";
		ctx.writeAndFlush(Unpooled.copiedBuffer(respData.getBytes()));
		//server端不用显示调用release(),是因为调用了writeAndFlush进行写操作
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
		ctx.close();
	}

}
