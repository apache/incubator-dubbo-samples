/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apache.dubbo.sample.basic;

import java.util.ArrayList;
import java.util.List;

import io.grpc.testing.integration.Messages.StreamingInputCallRequest;
import io.grpc.testing.integration.Messages.StreamingInputCallResponse;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.hello.HelloReply;
import org.apache.dubbo.hello.HelloRequest;

public class IStreamGreeterImpl implements IStreamGreeter {

    @Override
    public StreamObserver<HelloRequest> sayHello(StreamObserver<HelloReply> replyObserver) {

        return new StreamObserver<HelloRequest>() {
            private List<HelloReply> replyList = new ArrayList<>();

            @Override
            public void onNext(HelloRequest helloRequest) {
                System.out.println("onNext receive request name:" + helloRequest.getName());
                replyList.add(HelloReply.newBuilder()
                    .setMessage("receive name:" + helloRequest.getName())
                    .build());
            }

            @Override
            public void onError(Throwable cause) {
                System.out.println("onError");
                replyObserver.onError(cause);
            }

            @Override
            public void onCompleted() {
                System.out.println("onComplete receive request size:" + replyList.size());
                for (HelloReply reply : replyList) {
                    replyObserver.onNext(reply);
                }
                replyObserver.onCompleted();
            }
        };
    }
}
