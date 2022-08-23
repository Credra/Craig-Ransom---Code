// Name:Craig Ransom
// Student number:19037237
import java.util.ArrayList;
import java.util.List;
 
public class Graph {
 
	private List<Vertex> verticesList;

	public Graph() {
		this.verticesList = new ArrayList<>();
	}

	public void addVertex(Vertex vertex) {
		this.verticesList.add(vertex);
	}

	public void reset() {
		for(Vertex vertex : verticesList) {
			vertex.setVisited(false);
			vertex.setPredecessor(null);
			vertex.setDistance(Double.POSITIVE_INFINITY);
		}
	}

	////// Implement the methods below this line //////

	public List<Vertex> getShortestPath(Vertex sourceVertex, Vertex targetVertex) 
        {
            if(!pop(sourceVertex,targetVertex))
                return null;
            List<Vertex> l= new ArrayList<Vertex>();
            if(targetVertex.getDistance()==Double.POSITIVE_INFINITY)
                return l;
            Vertex check=targetVertex;
            while(check!=sourceVertex)
            {
                l.add(0, check);
                check=check.getPredecessor();
            }
            l.add(0, check);
            return l;
	}

	public double getShortestPathDistance(Vertex sourceVertex, Vertex targetVertex)
        {
            if(pop(sourceVertex,targetVertex))
                return targetVertex.getDistance();
            return Double.NEGATIVE_INFINITY;
	}
        
        private boolean pop(Vertex sourceVertex, Vertex targetVertex)
        {
            reset();
            List<Vertex> l= new ArrayList<Vertex>();
            sourceVertex.setDistance(0);
            l.add(sourceVertex);
            while(!l.isEmpty())
            {
                Vertex check=l.remove(0);
                for (int i = 0; i < check.getAdjacenciesList().size(); i++)
                {
                    Edge e=check.getAdjacenciesList().get(i);
                    if(e.getEndVertex().getDistance()>e.getStartVertex().getDistance() + e.getWeight())
                    {
                        
                        e.getEndVertex().setDistance(e.getStartVertex().getDistance() + e.getWeight());
                        e.getEndVertex().setPredecessor(e.getStartVertex());
                        if(!l.contains(e.getEndVertex()))
                            l.add(e.getEndVertex());
                        Vertex temp=e.getStartVertex().getPredecessor();
                        int k=verticesList.size();
                        while(temp!=null)
                        {
                            k--;
                            if(k<0)
                                return false;
                            temp=temp.getPredecessor();
                        }
                    }               
                }
            } 
            return true;
        }
}