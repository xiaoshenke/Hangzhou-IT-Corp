package wuxian.me.spidermaster.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import wuxian.me.spidermaster.rpc.RpcRequest;

import java.util.List;

/**
 * Created by wuxian on 26/5/2017.
 * <p>
 * https://my.oschina.net/huangyong/blog/361751
 * <p>
 * 解决tcp粘包
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;

    public RpcDecoder() {
        genericClass = RpcRequest.class;
    }

    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in, List<Object> out) throws Exception {

        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();  //Fixme: 为啥要close
        }
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        Object obj = SerializationUtil.deserialize(data, genericClass);
        out.add(obj);

    }
}
