package zad;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.*;
import org.jgroups.stack.ProtocolStack;
import org.jgroups.util.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class DistributedMap implements SimpleStringMap {
    private final HashMap<String, String> distributedHashMap;
    private JChannel channel;

    public DistributedMap() {
        this.distributedHashMap = new HashMap<>();
        configureJGroups();
        try {
            channel.getState(null, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureJGroups() {
        this.channel = new JChannel(false);
        ProtocolStack stack = new ProtocolStack();
        this.channel.setProtocolStack(stack);
        try {
            stack.addProtocol(new UDP().setValue("mcast_group_addr", InetAddress.getByName("230.0.0.68")))
                    .addProtocol(new PING())
                    .addProtocol(new MERGE3())
                    .addProtocol(new FD_SOCK())
                    .addProtocol(new FD_ALL()
                            .setValue("timeout", 12000)
                            .setValue("interval", 3000))
                    .addProtocol(new VERIFY_SUSPECT())
                    .addProtocol(new BARRIER())
                    .addProtocol(new NAKACK2())
                    .addProtocol(new UNICAST3())
                    .addProtocol(new STABLE())
                    .addProtocol(new GMS())
                    .addProtocol(new UFC())
                    .addProtocol(new MFC())
                    .addProtocol(new FRAG2())
                    .addProtocol(new SEQUENCER())
                    .addProtocol(new FLUSH())
                    .addProtocol(new STATE_TRANSFER());

            stack.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.channel.setReceiver(new ReceiverAdapter() {
            @Override
            public void viewAccepted(View view) {
                System.out.println(view.toString());
            }

            @Override
            public void receive(Message msg) {
                Map<String, String> hashmap = (Map<String, String>) msg.getObject();
                synchronized (DistributedMap.this.distributedHashMap) {
                    distributedHashMap.clear();
                    distributedHashMap.putAll(hashmap);
                }
            }

            @Override
            public void getState(OutputStream output) throws Exception {
                synchronized (distributedHashMap) {
                    Util.objectToStream(distributedHashMap, new DataOutputStream(output));
                }
            }

            @Override
            public void setState(InputStream input) throws Exception {
                Map<String, String> hashmap = (Map<String, String>) Util.objectFromStream(new DataInputStream(input));
                synchronized (distributedHashMap) {
                    distributedHashMap.clear();
                    distributedHashMap.putAll(hashmap);
                }
                System.out.println(distributedHashMap.size());
                System.out.println(distributedHashMap.toString());
            }
        });

        try {
            channel.connect("hashmap");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean containsKey(String key) {
        return distributedHashMap.containsKey(key);
    }

    @Override
    public String get(String key) {
        return distributedHashMap.get(key);
    }

    @Override
    public String put(String key, String value) {
        String result = distributedHashMap.put(key, value);
        Message msg = new Message(null, null, this.distributedHashMap);
        try {
            channel.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String remove(String key) {
        return distributedHashMap.remove(key);
    }
}
