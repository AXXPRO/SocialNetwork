package ro.ubbcluj.map.sem7.domain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clasa ce va fi folosita pentru a afla diferite informatii despre reteaua de utilizatori
 */
public class GrafUtilizatori {
    int V;
    HashMap<Long, Long> mapare = new HashMap<>();
    ArrayList<ArrayList<Integer>> adjListArray;
    long current = 0;
    long idLongest;
    long longest;

    ArrayList<Long> mapped;
    /**
     *
     * @param list - lista de utilizatori pe care sa facem diverse operatii
     */
    public GrafUtilizatori(ArrayList<Prietenie> list, int numUsers) {
        this.V = numUsers;
        mapped = new ArrayList<>();
        adjListArray = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adjListArray.add(i, new ArrayList<>());
        }
        list.forEach(el -> {
            if(!mapped.contains(el.getId().getLeft()))
            {
                mapare.put(el.getId().getLeft(),current++);
                mapped.add(el.getId().getLeft());
            }
        });
//        for (var el:list
//             ) {
//            mapare.put(el.getId(),current++);
//        }


            for(var prietenie : list)
            {
                addEdge( mapare.get(prietenie.getId().getLeft()), mapare.get(prietenie.getId().getRight()));

            }



    }

    ArrayList<Integer> currentLongest = new ArrayList<>();
    ArrayList<Integer> allTimeLongest = new ArrayList<>();
    /**
     *
     * @return lista cu cea mai sociabila comunitate
     */
    public ArrayList<Long> ceaMaiLungaComponentaConexa(){
        longest = 0;
        currentLongest.clear();
        allTimeLongest.clear();


        connectedComponents();

        //allTimeLongest now has the mapped values, need to "unmap them"

        ArrayList<Long> returnList = new ArrayList<>();

        for(var idUnmapped : allTimeLongest)
            {
                for (var entry : mapare.entrySet()) {
                    if (entry.getValue().intValue() == idUnmapped) {
                        returnList.add(entry.getKey());
                        break;
                    }
                }

            }


        return returnList;
    }

    // Adds an edge to an undirected graph
    private int nrComponenteConexe;


    /**
     *
     * @return numarul de componente conexe din graf
     */
    public int componenteConexe(){
        nrComponenteConexe =0;

        connectedComponents();
        return nrComponenteConexe;

    }

    /**
     * Se adauga muchia in graf
     * @param src - ID al unui nod
     * @param dest - al 2-ela ID a celulialt nod
     */
    void addEdge(long src, long dest) {

        adjListArray.get((int)src).add((int)dest);
        adjListArray.get((int)dest).add((int)src);
    }

    /**
     * Alogirtm de parcurgere DFS
     * @param v - nodul unde incepe
     * @param visited  - vector de elemente deja vizitate
     */
    void DFSUtil(int v, boolean[] visited) {
        currentLongest.add(v);
        // Mark the current node as visited and print it
        visited[v] = true;
  //      System.out.print(v + " ");
        // Recur for all the vertices
        // adjacent to this vertex

        adjListArray.get(v).forEach(x -> {
            if (!visited[x])
                DFSUtil(x, visited);

        });
//        for (int x : adjListArray.get(v)) {
//            if (!visited[x])
//                DFSUtil(x, visited);
//        }
    }

    /**
     * Algoritm ce va apela DFS
     */
    void connectedComponents() {

        boolean[] visited = new boolean[V];
        for (int v = 0; v < V; ++v) {
            if (!visited[v]) {

                nrComponenteConexe++;

                currentLongest.clear();

                DFSUtil(v, visited);
                if (currentLongest.size()  > allTimeLongest.size())
                {
                    allTimeLongest = new ArrayList<>(currentLongest);

                }


            //    System.out.println();
            }
        }
    }

}
