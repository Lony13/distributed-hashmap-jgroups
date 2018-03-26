package zad;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        System.setProperty("java.net.preferIPv4Stack", "true");
        Scanner scanner = new Scanner(System.in);
        DistributedMap distributedMap = new DistributedMap();
        boolean flag = true;

        distributedMap.put("Lony", "Discord");
        distributedMap.put("Ola", "Marcin");
        distributedMap.put("CDProject", "Marcin");
        while (flag){
            List<String> command = Arrays.asList(scanner.nextLine().split(" "));
            System.out.println("-----------------------------");
            System.out.println("[put] [key] [value]");
            System.out.println("[get|remove|contains] [key]");
            System.out.println("[show|members]");
            switch (command.get(0)){
                case "put":
                    System.out.println(distributedMap.put(command.get(1), command.get(2)));
                    break;
                case "remove":
                    System.out.println(distributedMap.remove(command.get(1)));
                    break;
                case "contains":
                    System.out.println(distributedMap.containsKey(command.get(1)));
                    break;
                case "get":
                    System.out.println(distributedMap.get(command.get(1)));
                    break;
                case "show":
                    System.out.println(distributedMap.getDistributedHashMap());
                    break;
                case "members":
                    System.out.println(distributedMap.getChannel().getView().getMembers());
                case "pass":
                    break;
                case "exit":
                    flag = false;
                    break;
            }
        }
    }
}
