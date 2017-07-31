#include <time.h>
#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <unistd.h>
#include <string.h>
#define pto "8999"
#define size 350

typedef struct datosCubeta {
  int numbers[size];
  int begin, end, saved, id;
  const char * port;
} DatosCubeta;

void createRandom(int numbers[size]) {
  srand(time(NULL));
  int i;
  for (i = 0; i < size; i++)
    numbers[i] = (rand() % 999) + 1;
}

void error(const char * msj){
 perror(msj);
 exit (1);
}//error

// get sockaddr, IPv4 or IPv6:
void *get_in_addr(struct sockaddr *sa)
{
    if (sa->sa_family == AF_INET) {
        return &(((struct sockaddr_in*)sa)->sin_addr);
    }

    return &(((struct sockaddr_in6*)sa)->sin6_addr);
}//gwt_in_addr

int connectToServer(const char * puerto) {
  struct addrinfo hints, *servinfo, *p;
  int cd, n1, rv, op = 0;
  char *srv = "127.0.0.1";
  memset(&hints, 0, sizeof hints);
  hints.ai_family = AF_UNSPEC;    /* Allow IPv4 or IPv6  familia de dir*/
  hints.ai_socktype = SOCK_STREAM;
  hints.ai_protocol = 0;
  if (!strcmp("9000", puerto))
    sleep(1);
  if ((rv = getaddrinfo(srv, puerto, &hints, &servinfo)) != 0) {
    fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
    return 1;
  }
  for(p = servinfo; p != NULL; p = p->ai_next) {
    if ((cd = socket(p->ai_family, p->ai_socktype,p->ai_protocol)) == -1) {
      perror("client: socket");
      continue;
    }
    if (connect(cd, p->ai_addr, p->ai_addrlen) == -1) {
      close(cd);
      perror("client: connect");
      continue;
    }
    break;
  }
  if (p == NULL) {
    fprintf(stderr, "client: error al conectar con el servidor\n");
    return 2;
  }
  freeaddrinfo(servinfo); // all done with this structure
  return cd;
}

int sendInt(int cd, int n) {
  int v;
  FILE *f = fdopen(cd, "w+");
  int toSend = htonl(n);
  v = write(cd, &toSend, sizeof(toSend));
  fflush(f);
}

int readInt(int cd) {
  int buffer = 0, readBytes = 0, readInteger = 0;
  while (readBytes < 4) {
    int n = read(cd, &buffer, (sizeof buffer) - readBytes);
    readInteger += (buffer << (8 * readBytes));
    readBytes += n;
  }
  return ntohl(readInteger);
}

int sendCubetas(int n) {
  struct addrinfo hints, *servinfo, *p;
  int cd,v,n1,rv,op=0;
  char *srv="127.0.0.1";
  memset(&hints, 0, sizeof hints);
  hints.ai_family = AF_UNSPEC;    /* Allow IPv4 or IPv6  familia de dir*/
  hints.ai_socktype = SOCK_STREAM;
  hints.ai_protocol = 0;
  if ((rv = getaddrinfo(srv, pto, &hints, &servinfo)) != 0) {
    fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
    return 1;
  }
  for(p = servinfo; p != NULL; p = p->ai_next) {
    if ((cd = socket(p->ai_family, p->ai_socktype,p->ai_protocol)) == -1) {
      perror("client: socket");
      continue;
    }
    if (connect(cd, p->ai_addr, p->ai_addrlen) == -1) {
      close(cd);
      perror("client: connect");
      continue;
    }
    break;
  }
  if (p == NULL) {
    fprintf(stderr, "client: error al conectar con el servidor\n");
    return 2;
  }
  freeaddrinfo(servinfo); // all done with this structure
  FILE *f = fdopen(cd,"w+");
  printf("Mandando numero de cubetas...\n");
  int toSend = htonl(n);
  v = write(cd, &toSend, sizeof(toSend));
  fflush(f);
}

void *cubeta(void *arg) {
  DatosCubeta* dCubeta = (DatosCubeta*)arg;
  int i;
  printf("Numeros guardados por cubeta %d total %d:\n", dCubeta -> id, dCubeta -> saved);
  for (i = 0; i < dCubeta -> saved; i++) {
    printf("%d\n", dCubeta -> numbers[i]);
  }
  int cd = connectToServer(dCubeta -> port);
  sendInt(cd, dCubeta -> saved);
  if (dCubeta -> saved != 0) {
    for (i = 0; i < dCubeta -> saved; i++) {
      sendInt(cd, dCubeta -> numbers[i]);
    }
    printf("Numeros ordenados de cubeta %d:\n", dCubeta -> id);
    for (i = 0; i < dCubeta -> saved; i++) {
      printf("%d\n", readInt(cd));
    }
  } else {
    printf("Hay 0 numeros guardados...\n");
  }
}

int main(int argc, char const *argv[]) {
  pthread_t * id_hilo;
  int cubetas = 0, i, j, c, numbers[size];
  puts("Ingresa el numero de cubetas:");
  scanf("%d", &cubetas);
  id_hilo = (pthread_t*)malloc(sizeof(pthread_t) * cubetas);
  DatosCubeta** datosCubeta = (DatosCubeta**)malloc(sizeof(DatosCubeta*) * cubetas);
  for (i = 0; i < cubetas; i++) {
    datosCubeta[i] = (DatosCubeta*)malloc(sizeof(DatosCubeta));
    datosCubeta[i] -> begin = 1 + ((1000 / cubetas) * i);
    datosCubeta[i] -> end = (1000 / cubetas) * (i + 1);
    datosCubeta[i] -> saved = 0;
    datosCubeta[i] -> id = i + 1;
    datosCubeta[i] -> port = (const char *)malloc(sizeof(const char) * 4);
    int tempPort = atoi(pto);
    tempPort += i + 1;
    sprintf((char * restrict)datosCubeta[i] -> port, "%d", tempPort);
  }
  if ((datosCubeta[cubetas - 1] -> end) != 1000) {
    datosCubeta[cubetas - 1] -> end = 1000;
  }
  for (i = 0; i < cubetas; i++) {
    printf("Rango cubeta %d: [%d, %d]\n", i + 1, datosCubeta[i] -> begin, datosCubeta[i] -> end);
  }
  createRandom(numbers);
  for (i = 0; i < size; i++) {
    for (c = 0; c < cubetas; c++) {
      if (numbers[i] >= datosCubeta[c] -> begin && numbers[i] <= datosCubeta[c] -> end) {
        datosCubeta[c] -> numbers[datosCubeta[c] -> saved] = numbers[i];
        (datosCubeta[c] -> saved)++;
        c = cubetas + 1;
      }
    }
  }
  sendCubetas(cubetas);
  for (i = 0; i < cubetas; i++) {
    printf("Cubeta %d tiene: %d numeros guardados\n", i + 1, datosCubeta[i] -> saved);
    pthread_create(&id_hilo[i], NULL, cubeta, (void*) datosCubeta[i]);
    pthread_join(id_hilo[i], NULL);
  }
  /*for (i = 0; i < cubetas; i++) {
    pthread_join(id_hilo[i], NULL);
  }*/
  return 0;
}
