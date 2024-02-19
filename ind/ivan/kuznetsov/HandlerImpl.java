package ind.ivan.kuznetsov;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HandlerImpl implements Handler {

    private final ExecutorService service;
    private final Client client;
    public HandlerImpl(Client client) {
        this.service = Executors.newVirtualThreadPerTaskExecutor();
        this.client = client;
    }

    @Override
    public Duration timeout() {
        return Duration.ofMillis(0);
    }

    @Override
    public void performOperation() {
        var event = client.readData();
        event.recipients().parallelStream().forEach(x->{
            service.execute(()->sendData(x, event.payload()));;
        });
    }
    private void sendData(Address a, Payload p){
        var result = client.sendData(a,p);
        if(result == Result.ACCEPTED){
            return;
        } else {
            try {
                Thread.sleep(timeout().toMillis());
                sendData(a, p);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            
        }
    }
}
