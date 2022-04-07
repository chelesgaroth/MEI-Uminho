/**
TPC 4 - Circuitos Eulerianos
Raquel Costa - pg47600

Objetivo: Usar o Alloy para encontrar um circuito Euleriano num grafo não orientado e ligado (sem lacetes). Definir um comando "run" que 
exemplifica com um grafo completo com 5 nodos - K5.

Solução: Com base na pesquisa realizada será usado o algoritmo de Fleury para descobrir um circuito euleriano.

Passos:
(1)  Make sure the graph has 0 odd vertices. (de modo a ter a certeza que existe um circuito euleriano)
(2)  If there are 0 odd vertices, start anywhere.
(3)  Follow edges one at a time. If you have a choice between a bridge and a non-bridge, always choose the non-bridge.
(Mas como estamos a tratar de grafos completos então não existem "bridges", uma vez que nos grafos completos, cada vértice conecta-se
a todos os outros.)
(4)  Stop when you run out of edges.

**/

abstract sig EdgeStatus {}          // Tells if edge has already been travelled (removed) or not
one sig Visited, NotVisited extends EdgeStatus {}

sig Vertex {
    adj : set Edge                  // Edges of the vertex
}

sig Edge {
    vertexDest : one Vertex,        // Vertex no. 2
    var status  : one EdgeStatus    // Travelled or not
}

var sig VertexInicial in Vertex {}  //Beginnig and end of the circuit
var sig VertexAtual in Vertex {}    //Current Vertex 


/* Initial conditions for the system :
*/
fact Init {

    no VertexAtual
    no VertexInicial
    
    // Inicialmente todas as arestas estão no estado não visitado
    all e : Edge | e.status = NotVisited

    // O grafo deve ser completo, i.e, cada vértice está ligado aos restantes.
    all v : Vertex | Vertex-v in v.adj.vertexDest

    // O grafo não deve ter lacetes
    all e : Edge | adj.e != e.vertexDest
    
    // Os vértices de um grafo devem ser todos par
    all v : Vertex | rem[#v.adj, 2] = 0

    // Uma aresta apenas pode possuir um nodo inicial
    all e : Edge | one adj.e

}



// Escolher o vértice inicial
pred initiate [v : Vertex] {
    //Guards
    no VertexInicial
    no VertexAtual
    Edge.status = NotVisited

    //Effects
    VertexInicial' = v
    VertexAtual' = v

    //Frame Conditions
    status' = status
}

// Escolhe a próxima aresta 
pred goToNextEdge [e : Edge] {
    //Guards
    e in VertexAtual.adj
    e.status = NotVisited

    //Effects
    e.status' = Visited
    all e1 : Edge - e | (adj.e1 = e.vertexDest and adj.e = e1.vertexDest) implies e1.status' = Visited
    VertexAtual' = e.vertexDest

    //Frame Conditions 
    VertexInicial' = VertexInicial
    all e2 : Edge - e | (adj.e2 != e.vertexDest or e2.vertexDest != adj.e) implies e2.status' = e2.status
}

//Edge.status = Visited
//VertexAtual = VertexInicial
pred nop [] {

    VertexInicial' = VertexInicial
    VertexAtual' = VertexAtual
    status' = status
}

fact Traces {
    always (nop or
                (some v : Vertex | initiate[v]) or
                (some e : Edge | goToNextEdge[e]))
}


// Cenário de exemplo com um grafo completo com 5 nodos - K5
run Exemplo {

} for exactly 5 Vertex , 20 Edge , 20 steps