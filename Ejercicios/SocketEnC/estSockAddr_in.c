
int main(int argc, char const *argv[]) {
  struct sockaddr_in sdir;
  memset(sdir.sin_zero, 0, 8);
  sdir.sin_family = AF_INET;
  sdir.sin_port = htons(1234);
  sdir.sin_addr.
  int s = socket(AF_INET, SOCKSTREAM, 6);
  if (s, (struct sockaddr, x)) {
    perror("El puerto ya esta ");
    close(s);
  }
  return 0;
}
