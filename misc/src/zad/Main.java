package zad;

public class Main {
    public static void main(String[] args){
        System.setProperty("java.net.preferIPv4Stack", "true");
        DistributedMap distributedMap = new DistributedMap();
        distributedMap.put("Lony", "Discord");
        distributedMap.put("Ola", "Marcin");
        distributedMap.put("CDProject", "Marcin");
        while (true){
            System.out.println("-----------------------------");
            System.out.println(distributedMap.getDistributedHashMap());
            System.out.println(distributedMap.getDistributedHashMap().size());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
