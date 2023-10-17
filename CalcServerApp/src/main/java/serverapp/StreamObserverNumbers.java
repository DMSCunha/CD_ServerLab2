package serverapp;

import calcstubs.Result;
import calcstubs.Number;
import io.grpc.stub.StreamObserver;


public class StreamObserverNumbers implements StreamObserver<Number> {

    StreamObserver<Result> responseObserver;
    private int sum = 0;

    public StreamObserverNumbers(StreamObserver<Result> responseObserver) {
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(Number number) {
        sum += number.getNum();
        System.out.println("Received number = " + number.getNum()+"\nSum is now "+ sum);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

        System.out.println("Final sum is "+ sum);

        //build result to send
        Result result = Result.newBuilder()
                .setId("Sum")
                .setRes(sum)
                .build();

        //send result to client
        responseObserver.onNext(result);
        responseObserver.onCompleted();
        sum = 0;
    }
}
