package serverapp;

import calcstubs.*;
import calcstubs.Number;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;


public class Server extends CalcServiceGrpc.CalcServiceImplBase {

    private static int svcPort = 8500;

    public static void main(String[] args) {
        try {
            if (args.length > 0) svcPort = Integer.parseInt(args[0]);
            io.grpc.Server svc = ServerBuilder
                .forPort(svcPort)
                .addService(new Server())
                .build();
            svc.start();
            System.out.println("Server started, listening on " + svcPort);
            //Scanner scan = new Scanner(System.in);
            //scan.nextLine();
            svc.awaitTermination();
            svc.shutdown();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    //case 1 unary response (request server, response client)
    public void add(AddOperands request, StreamObserver<Result> responseObserver){

        //build result to send
        Result result = Result.newBuilder()
                .setId(request.getId())
                .setRes(request.getOp1()+ request.getOp2())
                .build();

        //debug
        System.out.println("\t--- Case 1 ---");
        System.out.println("Received -> Op1: "+ request.getOp1()+ " Op2: "+ request.getOp2());
        System.out.println("Sent -> result = "+ (request.getOp1()+ request.getOp2()));

        //send result to client
        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }


    @Override
    //case 2 request client, stream server
    public void generatePowers(NumberAndMaxExponent request, StreamObserver<Result> responseObserver) {

        //generate the random max exponent
        int rand = (int)Math.floor(Math.random()*(request.getMaxExponent() - 1 + 1) + 1);

        //debug
        System.out.println("\t--- Case 2 ---");
        System.out.println("Received -> Base number: "+request.getBaseNumber()+" maxRandValue: "+request.getMaxExponent());

        //calc all values from one to random max exponent
        for(int i = 1; i <= rand; i++){
            //value calc with i
            int numbResult = (int) Math.pow(request.getBaseNumber(), rand);

            //debug
            System.out.println("Sending: "+ numbResult+" = "+request.getBaseNumber()+"^"+i);

            //build result to send
            Result result = Result.newBuilder()
                    .setId(request.getId())
                    .setRes(numbResult)
                    .build();

            //send result to client
            responseObserver.onNext(result);
        }

        //send completed
        responseObserver.onCompleted();
    }


    @Override
    //case 3 stream client, response server
    public StreamObserver<Number> addSeqOfNumbers(StreamObserver<Result> responseObserver) {

        // MIGHT BE WRONG
        System.out.println("--- Case 3 ---");
        return new StreamObserverNumbers(responseObserver);
    }

    @Override
    //case 4 bidirectional stream
    public StreamObserver<AddOperands> multipleAdd(StreamObserver<Result> responseObserver) {

        //MIGHT BE WRONG
        System.out.println("--- Case 4 ---");
        return new SteamObserverAddOperands(responseObserver);
    }
}
