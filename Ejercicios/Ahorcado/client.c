#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h> //getaddrinfo() getnameinfo() freeaddrinfo()
#include <string.h>
#include <unistd.h>//read
#include <stdlib.h>//exit
#define pto "9999"

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


int main(){

 struct addrinfo hints, *servinfo, *p;
 int cd,n,n1,rv,op=0, difR = 0;
 char *srv="2001::3333:1";

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

	/*if (setsockopt(cd, IPPROTO_IPV6, IPV6_V6ONLY, (void *)&op, sizeof(op)) == -1) {
            perror("setsockopt   no soporta IPv6");
            exit(1);
        }*/

        if (connect(cd, p->ai_addr, p->ai_addrlen) == -1) {
            close(cd);
            perror("client: connect");
            continue;
        }

        break;
    }//for

    if (p == NULL) {
        fprintf(stderr, "client: error al conectar con el servidor\n");
        return 2;
    }

    freeaddrinfo(servinfo); // all done with this structure

FILE *f = fdopen(cd,"w+");
 printf("Conexionen establecida.. Escribe una serie de cadenas <enter> para enviar, SALIR para terminar\n");

  char *linea=(char *)malloc(sizeof(char)*50);
  bzero(linea,sizeof(linea));
  size_t tam;
  char eco[100];
  puts("Ingresa la dificultad");
	scanf("%d", &difR);
	n1= write(cd,&difR,sizeof(difR));
 	fflush(f);
	bzero(eco,sizeof(eco));
 	n1=read(cd,eco,sizeof(eco));
	printf("tamaño palabra: %d",(int)sizeof(eco) + 1);
  if(n1<0)
    error("Error al leer desde el socket\n");
  else if(n1==0)
    error("Socket cerrado\n");
  printf("\nPalabra recibida: %s\n",eco);
  fflush(f);
  fclose(f);
  close(cd);
  return 0;
}//main
