package wuxian.me.spidermaster.agent;


import com.sun.istack.internal.NotNull;
import wuxian.me.spidermaster.rpc.IRpcCallback;
import wuxian.me.spidermaster.rpc.RpcRequest;
import wuxian.me.spidermaster.rpc.RpcResponse;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by wuxian on 10/6/2017.
 */
public class RequestSender {

    private Map<String, IRpcCallback> requestMap = new ConcurrentHashMap<String, IRpcCallback>();
    private Queue<RpcRequest> requestQueue = new LinkedBlockingQueue<RpcRequest>();
    private IClient client;

    private Thread dispatchThread;

    public RequestSender(@NotNull IClient client) {

        this.client = client;
    }

    public void init() {
        dispatchThread = new Thread() {
            @Override
            public void run() {

                while (true) {
                    while (true) {

                        if (!requestQueue.isEmpty()) {
                            client.channel().write(requestQueue.poll());
                        }

                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {

                        }
                    }
                }

            }
        };
        dispatchThread.setName("dispatchRpcRequestThread");
        dispatchThread.start();
    }


    public void put(RpcRequest request, IRpcCallback onRpcReques) {
        if (request == null) {
            return;
        }

        if (requestMap.containsKey(request.requestId)) {
            return;
        }

        requestMap.put(request.requestId, onRpcReques);
        requestQueue.add(request);
    }

    //运行在"ConnectionThread..." --> Fixme:进行优化
    public void onRpcResponse(RpcResponse response) {
        if (response == null || response.requestId == null || response.requestId.length() == 0) {
            return;
        }

        if (!requestMap.containsKey(response.requestId)) {
            return;
        }

        //Todo
        requestMap.get(response.requestId).onResponseSuccess();
    }
}