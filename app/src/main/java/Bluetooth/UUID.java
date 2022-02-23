package Bluetooth;

import static java.util.UUID.fromString;

public class UUID {

    public static final java.util.UUID
            // Service  UUIDs
            UUID_SERVICE_KZ_CONTROL = fromString("149d50d7-bb21-4fb8-8e94-2dcd278bacf3"),
            UUID_SERVICE_KZ_STATUS = fromString("96237dec-e596-45f3-b1ea-b33602873df2"),
            UUID_SERVICE_ENVIRONMENT_SENSING = fromString("fc779b34-b4c4-4ccc-827d-6ef2bd594f6a"),
    //Custom services
    UUID_SERVICE_FREQUENCY = fromString("3ea3737d-0f0d-4f1b-ba34-664b2e4a3b53"),
            UUID_SERVICE_INTENSITY = fromString("9d8c77fc-3407-468e-bbca-8c731b4da66f"),
            UUID_SERVICE_TIME = fromString("953df93a-6cf9-4a5d-9b25-ad2ce9104e2a"),
            UUID_SERVICE_TRANSDUCERS = fromString("86499f53-6b12-4e3b-9639-c552530dde28"),
            UUID_SERVICE_COMMANDS = fromString("c9703274-4cb1-476e-aaa3-595a8f7537b5"),
            UUID_SERVICE_PROCESS_VALUE = fromString("90d6d8e7-3b75-4dd4-91b5-4b83fb69daf5"),

    // UUID_SERVICE_HEALTH_THERMOMETER=fromString("0x1809"),


    // Characteristic UUIDs
    // UUID_CHARACTERISTIC_TEMPERATURE_MEASURING = fromString("0x2A1C"),
    UUID_CHARACTERISTIC_HUMIDITY = fromString("d0c1c7e5-a734-4cbd-be78-b3574123fcdf"), // Environment Service
            UUID_CHARACTERISTIC_KZ_CONTROL = fromString("975843f3-30dd-4e59-9efe-d8617cb7519f"),
    //Custom Characteristic
    UUID_CHARACTERISTIC_FREQUENCY = fromString("8f857530-2246-41b3-a6e2-4f53c083f367"),
            UUID_CHARACTERISTIC_INTENSITY = fromString("3ca1c1d5-7a07-4242-8ae4-c6b990fd0626"),
            UUID_CHARACTERISTIC_TIME = fromString("76afe072-b93c-46ab-b1d8-70752cc015af"),
            UUID_CHARACTERISTIC_TRANSDUCERS = fromString("b92f3be1-11fa-462e-87e5-449d506122f0"),
            UUID_CHARACTERISTIC_COMMANDS = fromString("012a4bbe-043e-4259-97f2-85564e371b21"),
            UUID_CHARACTERISTIC_PROCESS_VALUE = fromString("3585173c-1fc8-4cb5-a214-6ff08aa50f63"),
            UUID_CHARACTERISTIC_KZ_STATUS = fromString("bd07f5b7-5844-4101-acbb-10475fe60134");


}
//UUID_SERVICE_FREQUENCY = fromString("3ea3737d-0f0d-4f1b-ba34-664b2e4a3b53"),
//UUID_CHARACTERISTIC_FREQUENCY = fromString("8f857530-2246-41b3-a6e2-4f53c083f367"),