package com.lucky.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.logging.Logger;

/**
 * 描述:
 * netty client
 *
 * @author luzhongfu
 * @create 2019-04-20 1:00 PM
 */
public class NettyClient {

    private final static Logger LOGGER = Logger.getLogger("NettyClient");

    public void nettyBind(int port, String host){
        NioEventLoopGroup loop = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(loop)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
                            pipeline.addLast(new DelimiterBasedFrameDecoder(1024,buf));
//                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new ClientChannelHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(host,port).sync();
            future.channel().closeFuture().sync();
        }catch (Exception e){
            LOGGER.info("server start error:" + e.getMessage());
        }finally {
            loop.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 9090;
        String host = "127.0.0.1";
        if(args != null && args.length != 0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (Exception e){
                LOGGER.info("port request error: "+ e.getMessage());
            }
        }
        new NettyClient().nettyBind(port,host);
    }
}
