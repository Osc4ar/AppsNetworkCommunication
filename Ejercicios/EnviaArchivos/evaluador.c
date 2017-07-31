#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "Pila.h"

struct nodoF
{
  char dato;
  struct nodoF *ptrSig;
};
typedef struct nodoF NodoF;

struct inodo
{
  int dato;
  struct inodo *ptrSig;
};
typedef struct inodo iNodo;

NodoF* newNodoF(int n)
{
  NodoF *this = (NodoF*)malloc(sizeof(NodoF));
  this -> dato = n;
  this -> ptrSig = NULL;
  return this;
}

iNodo* newiNodo(int n)
{
  iNodo *this = (iNodo*)malloc(sizeof(iNodo));
  this -> dato = n;
  this -> ptrSig = NULL;
  return this;
}

void pushF(NodoF* ptrTop, char dato)
{
  NodoF *ptrNuevo;

  ptrNuevo = newNodoF(dato);
  if(ptrTop -> ptrSig == NULL)
  {
    ptrNuevo -> ptrSig = NULL;
    ptrTop -> ptrSig = ptrNuevo;
  }
  else
  {
    ptrNuevo -> ptrSig = ptrTop -> ptrSig;
    ptrTop -> ptrSig = ptrNuevo;
  }
}

void iPush(iNodo* ptrTop, char dato)
{
  iNodo *ptrNuevo;

  ptrNuevo = newiNodo(dato);
  if(ptrTop -> ptrSig == NULL)
  {
    ptrNuevo -> ptrSig = NULL;
    ptrTop -> ptrSig = ptrNuevo;
  }
  else
  {
    ptrNuevo -> ptrSig = ptrTop -> ptrSig;
    ptrTop -> ptrSig = ptrNuevo;
  }
}

char popF(NodoF *ptrTop)
{
  NodoF *ptrAux = (NodoF*)malloc(sizeof(NodoF));
  char out = ' ';
  if(ptrTop -> ptrSig == NULL)
    puts("  La pila esta vacia");
  else
  {
    ptrAux = ptrTop -> ptrSig;
    ptrTop -> ptrSig =  ptrAux -> ptrSig;
    out = ptrAux -> dato;
    free(ptrAux);
  }
  return out;
}

char iPop(iNodo *ptrTop)
{
  iNodo *ptrAux = (iNodo*)malloc(sizeof(iNodo));
  char out = ' ';
  if(ptrTop -> ptrSig == NULL)
    puts("  La pila esta vacia");
  else
  {
    ptrAux = ptrTop -> ptrSig;
    ptrTop -> ptrSig =  ptrAux -> ptrSig;
    out = ptrAux -> dato;
    free(ptrAux);
  }
  return out;
}

int precedence(char op)
{
  if(op == '^')
    return 3;
  if(op == '*' || op == '/')
    return 2;
  if(op == '+' || op == '-')
    return 1;
  return 0;
}

void convert(char *in, char *out, NodoF *ptrTop)
{
  int size = strlen(in), i = 0, j = 0;
  in[size] = ')';
  in[size + 1] = '\0';
  while (ptrTop -> ptrSig != NULL)
  {
    if((in[i] >= '0' && in[i] <= '9') || (in[i] >= 'A' && in[i] <= 'Z') || (in[i] >= 'a' && in[i] <= 'z'))
    {
      out[j] = in[i];
      j++;
    }

    if(in[i] == '(')
    {
      pushF(ptrTop, in[i]);
    }

    if(in[i] == '^' || in[i] == '*' || in[i] == '/' || in[i] == '+' || in[i] == '-')
    {
      out[j] = ',';
      j++;
      if(ptrTop -> ptrSig != NULL)
      {
        char ot = popF(ptrTop);
        if(precedence(ot) < precedence(in[i]))
          pushF(ptrTop, ot);
        else
        {
          while(precedence(ot) >= precedence(in[i]) && ot != 'o')
          {
            out[j] = ot;
            j++;
            out[j] = ' ';
            j++;
            ot = popF(ptrTop);
            if(precedence(ot) < in[i])
            {
              pushF(ptrTop, ot);
              ot = 'o';
            }
          }
        }
        pushF(ptrTop, in[i]);
      }
      else
        pushF(ptrTop, in[i]);
    }

    if(in[i] == ')')
    {
      out[j] = ',';
      j++;
      char ot = popF(ptrTop);
      while(ot != '(')
      {
        out[j] = ot;
        j++;
        ot = popF(ptrTop);
      }
    }
    i++;
  }
  out[j] = '\0';
  i = 1;
  while(out[i])
  {
    if(out[i] == '+' && out[i + 1] == '-')
    {
      out[i] = '-';
      out[i + 1] = '+';
    }
    if(out[i] == ',' && (out[i - 1] == '^' || out[i - 1] == '*' || out[i - 1] == '/' || out[i - 1] == '+' || out[i - 1] == '-'))
      out[i] = ' ';
    i++;
  }
}

int toInt(char c[])
{
  int v = 0, i = strlen(c) - 1, j = 0;
  while(c[j])
  {
    if(c[j] >= 48 && c[j] <= 57)
      v += (c[j] - 48) * pow(10, i);
    i--;
    j++;
  }
  return v;
}

int evaluate(char expr[], iNodo *ptrTop)
{
  char in[1024];
  int i = 0, j = 0;

  while(ptrTop -> ptrSig != NULL)
    iPop(ptrTop);

  while(expr[i])
  {
    if(expr[i] >= '0' && expr[i] <= '9')
    {
      in[j] = expr[i];
      j++;
    }

    if(expr[i] == ',')
    {
      in[j] = '\0';
      j = 0;
      iPush(ptrTop, toInt(in));
    }

    if(expr[i] == '^' || expr[i] == '*' || expr[i] == '/' || expr[i] == '+' || expr[i] == '-')
    {
      int a = iPop(ptrTop);
      int b = iPop(ptrTop);

      if(expr[i] == '^')
        iPush(ptrTop, pow(b, a));
      if(expr[i] == '*')
        iPush(ptrTop, b * a);
      if(expr[i] == '/')
        iPush(ptrTop, b / a);
      if(expr[i] == '+')
        iPush(ptrTop, b + a);
      if(expr[i] == '-')
        iPush(ptrTop, b - a);
    }

    i++;
  }
  return iPop(ptrTop);
}

int parentesis(char* str)
{
  Nodo* top = init();
  push(top, 'Z');
	int i;
	char state = 'q';
	for (i = 0; str[i]; i++)
	{
		if (str[i] == '(')
		{
			push(top, 'X');
			state = 'q';
		}
		if (str[i] == ')')
		{
			pop(top);
			state = 'p';
		}
	}
	if(pop(top) == 'Z')
	{
		push(top, 'Z');
		pop(top);
		return 1;
	}
	return 0;
}

int main()
{
  NodoF *ptrTop = (NodoF*)malloc(sizeof(NodoF));
  ptrTop -> dato = ':';
  ptrTop -> ptrSig = NULL;

  iNodo *iPtrTop = (iNodo*)malloc(sizeof(iNodo));
  iPtrTop -> dato = 0;
  iPtrTop -> ptrSig = NULL;

  char input[1024], output[1024];
  int o = 0;
  do {
    puts("1) Ingresar cadena");
    puts("2) Salir");
    scanf("%d", &o);

    switch (o)
    {
      case 1:
        o = 0;
        while(ptrTop -> ptrSig != NULL)
          popF(ptrTop);
        output[0] = '\0';
        pushF(ptrTop, '(');
        getchar();
        puts("Ingresa la expresion:");
        fgets(input, 1024, stdin);
        if (parentesis(input))
        {
          convert(input, output, ptrTop);
          printf("El resultado es: %d\n", evaluate(output, iPtrTop));
        }
      	else
      		puts("\nParentesis desbalanceados\n");
      break;
    }
  } while(o != 2);
  return 0;
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     