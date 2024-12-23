dependencies {
    implementation("org.springframework.boot:spring-boot-starter:2.6.3")
    implementation ("org.springframework.kafka:spring-kafka:2.8.4")
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.3")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.3")
    implementation("org.postgresql:postgresql:42.2.22")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}