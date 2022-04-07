abstract sig EdgeStatus {}       // Tells if edge has already been travelled (removed) or not
one sig Visited, NotVisited extends EdgeStatus {}

abstract sig EdgeBridge {}       // If a edge is a bridge or not
one sig Bridge, NoBridge extends EdgeBridge {}


sig Graph {
    vertices : set Vertex,       // Number of vertices
    allAdj  : set Edge,          // Adjacency list
    var path : set Edge            // Caminho euleriano do grafo
}

sig Vertex {
    var adj : set Vertex        // vértices adjacentes
}

sig Edge {
    vertex1 : one Vertex,        // Vertex no. 1
    vertex2 : one Vertex,        // Vertex no. 2
    status  : one EdgeStatus,     // Travelled or not
    isBridge : one EdgeBridge    // Bridge or not
}