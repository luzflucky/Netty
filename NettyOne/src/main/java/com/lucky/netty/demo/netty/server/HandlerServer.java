package com.lucky.netty.demo.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.EventExecutorGroup;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created by lucky on 2018/6/7.
 */
public class HandlerServer extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
//        ByteBuf buffer = (ByteBuf) msg;
//        byte[] bytes = new byte[buffer.readableBytes()];
//        buffer.readBytes(bytes);
//        String body = new String(bytes,"UTF-8");
//        System.out.println("this is client send:"+body);
//        String time = "client ni hao a";
        System.out.println("this is client send:"+msg);
//        String time = "client ni hao a"+System.getProperty("line.separator");
        String time = "client ni hao a $_";
        ByteBuf buf = Unpooled.copiedBuffer(time.getBytes());
        ctx.writeAndFlush(buf);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
