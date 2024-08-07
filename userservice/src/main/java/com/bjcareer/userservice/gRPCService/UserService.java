package com.bjcareer.userservice.gRPCService;


import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import com.bjcareer.grpc.userservice.v0.UserMessageProto.*;
import com.bjcareer.grpc.userservice.v0.UserserviceGrpc;


//@GrpcService
public class UserService extends UserserviceGrpc.UserserviceImplBase{

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        String username = request.getUsername();
        String password = request.getPassword();

        System.out.println("password = " + password);
        System.out.println("username = " + username);

        LoginResponse res = LoginResponse.newBuilder().setMessage("Test").setSuccess(true).build();

        responseObserver.onNext(res);
        responseObserver.onCompleted();

    }
}