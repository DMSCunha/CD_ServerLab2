package serverapp;

import calcstubs.Result;
import io.grpc.stub.StreamObserver;
import calcstubs.AddOperands;

import java.sql.SQLOutput;

public class SteamObserverAddOperands implements StreamObserver<AddOperands> {

    StreamObserver<Result> responseObserver;

     public SteamObserverAddOperands(StreamObserver<Result> responseObserver){
        this.responseObserver = responseObserver;
     }

    @Override
    public void onNext(AddOperands addOperands) {

         //debug
         System.out.println("Received Op1 = "+addOperands.getOp1()+" Op2 = "+addOperands.getOp2());
         System.out.println("Sent result = "+ (addOperands.getOp1()+ addOperands.getOp2()));

         //Build result to send
         Result result = Result.newBuilder()
                .setId(addOperands.getId())
                .setRes(addOperands.getOp1()+ addOperands.getOp2())
                .build();


         //send result BUT not completed (case 4)
         responseObserver.onNext(result);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

        System.out.println("All sums done and sent");
        responseObserver.onCompleted();
    }
}
