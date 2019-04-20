package com.lucky.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 描述:
 * netty client handler
 *
 * @author luzhongfu
 * @create 2019-04-20 1:07 PM
 */
public class ClientChannelHandler extends ChannelHandlerAdapter {

    private ByteBuf messageBuf;
    private byte[] message;


    public ClientChannelHandler(){
        message = "你好呀,你在干什么呢$_".getBytes();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        byte[] result = new byte[buf.readableBytes()];

        buf.readBytes(result);

        String serverSay = new String(result,"UTF-8");

        System.out.println("client accept server say:");

        System.out.println(serverSay);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0 ; i < 10 ; i ++){
            messageBuf = Unpooled.buffer(message.length);

            messageBuf.writeBytes(message);
            ctx.writeAndFlush(messageBuf);
        }
        System.out.println("client send success");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
