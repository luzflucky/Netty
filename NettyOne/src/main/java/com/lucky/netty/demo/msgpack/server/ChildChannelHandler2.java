package com.lucky.netty.demo.msgpack.server;


import com.lucky.netty.demo.msgpack.MsgPackDecoder;
import com.lucky.netty.demo.msgpack.MsgPackEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class ChildChannelHandler2 extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
    	//编码
		ch.pipeline().addLast("msgpack decoder",new MsgPackDecoder());
    	//加2个空字节分割
    	ch.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
    	//解码
    	ch.pipeline().addLast("msgpack encoder",new MsgPackEncoder());
		
		ch.pipeline().addLast(new ServerHanlder2());
		System.out.println("server ok");
		
	}

	

}
