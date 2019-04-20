package com.lucky.netty.server;

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
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.logging.Logger;

/**
 * 描述:
 * netty server
 *
 * @author luzhongfu
 * @create 2019-04-20 12:41 PM
 */
public class NettyServer {

    private final static Logger LOGGER = Logger.getLogger("NettyServer");

    public void nettyBind(int port){
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss,work)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            //自定义字符 拆包
                            ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
                            pipeline.addLast(new DelimiterBasedFrameDecoder(1024,buf));
                            //使用string 转码
//                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new ServerChannelHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();

            future.channel().closeFuture().sync();
        }catch (Exception e){
            LOGGER.info("server start error:" + e.getMessage());
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 9090;
        if(args != null && args.length != 0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (Exception e){
                LOGGER.info("port request error: "+ e.getMessage());
            }
        }
        new NettyServer().nettyBind(port);
    }
}
