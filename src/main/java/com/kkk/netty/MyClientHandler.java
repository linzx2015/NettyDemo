package com.kkk.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * 客户端发送和接收数据的处理类
 * @author linzx
 * */
public class MyClientHandler extends ChannelHandlerAdapter
{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
		try{
//			ByteBuf buf=(ByteBuf) msg;//ByteBuf 有读和写两个指针
//			byte[] recData=new byte[buf.readableBytes()];
//			buf.readBytes(recData);
//			
//			String getMsg=new String(recData,"utf-8");
//			System.out.println("client receive data:"+getMsg);
			String recData=(String) msg; //在Client端设置了StringDecoder,此处可直接强转
			System.out.println("server receive:"+recData);
		} finally{
			ReferenceCountUtil.release(msg);
			//byteBuf是引用计数对象,必须显示释放,否则内存会被撑爆
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
		ctx.close();
	}

}
