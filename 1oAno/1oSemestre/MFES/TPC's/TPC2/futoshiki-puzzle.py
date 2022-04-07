'''
Desenvolva um programa em Phyton para resolver este jogo com o auxílio de um SMT solver:

• Input: a configuração do tabuleiro inicial deverá ser fornecida num ficheiro de texto, em
formato que entendam adquado para o descrever. Como alternativa, pode receber o texto do
tabuleiro diretamente numa string.

• Output: a solução do puzzle deverá ser impressa no ecrã.
'''

from z3 import *

sol = Solver()

with open("puzzle.txt") as file:
    lines = file.readlines()
    aux = [line.rstrip().split() for line in lines]

print(aux)

N = int(aux[0][0])
#print(N)
tab = []
for i in range(N):
	tab.append(aux[i+1])

tab = list(map(list, (map(int, x) for x in tab)))
#print(tab)

cond = []
for i in range(len(aux)-N-1):
	cond.append(aux[N+1+i])

print(cond)

X = [ [Int("x_%s_%s" % (i,j)) for j in range(N)] for i in range(N)]
#print(X)

lenCond = len(cond)
for i in range(lenCond):
	sol.add(X[int(cond[i][0])][int(cond[i][1])] > X[int(cond[i][3])][int(cond[i][4])])

sol.add( [ And(X[i][j] >= 1, X[i][j] <= N) for j in range(N) for i in range(N)] )
sol.add( [ If(tab[i][j] == 0, True, X[i][j] == tab[i][j]) for j in range(N) for i in range(N) ] )
sol.add( [ Distinct(X[i]) for i in range(N)])
sol.add( [Distinct([X[i][j] for i in range(N) ]) for j in range(N)] )


if sol.check() == sat:
	m = sol.model()
	r = [ [ m.evaluate(X[i][j]) for j in range(N) ]
		for i in range(N) ]
	print_matrix(r)
	#print(np.matrix(r))
else:
	print ("Puzzle impossível.")
