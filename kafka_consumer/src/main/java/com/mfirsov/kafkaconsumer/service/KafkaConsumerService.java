package com.mfirsov.kafkaconsumer.service;

//@Service
//@Log4j2
//@KafkaListener(topics = "${kafka.topic:bank_account}", containerFactory = "kafkaListenerContainerFactory")
public class KafkaConsumerService {

//    @Autowired
//    private BankAccountRepository bankAccountRepository;
//
//    Queue<BankAccount> bankAccounts = new LinkedList<>();
//    int k=0;
//
//    @KafkaHandler
//    public void writeDataFromKafkaToQueue(@Payload BankAccount bankAccount) {
//        bankAccounts.offer(bankAccount);
//        log.info("Send to Queue: " + bankAccount);
//    }
//
//    @Scheduled(fixedRate = 32000)
//    public void writeFromQueueToCassandra() {
//        while (!bankAccounts.isEmpty()) {
//            BankAccount bankAccount = bankAccounts.poll();
//            log.info("Write following item into Cassandra: #" + k + " " + bankAccount);
//            bankAccountRepository.insert(bankAccount);
//            k++;
//        }
//    }



}
