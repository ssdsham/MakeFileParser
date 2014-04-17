/*Sham Dorairaj, 24-Nov-2013, 15:23 
 *Version: 3
 *Description: Parses a makefile(here, string) and topologically sorts based on dependencies and returns via makeTarget method.
 *Parses the input and creating a graph in memory and manipulates the graph to produce a list of commands that need to be executed to produce a given target. 
 *The Builder class is implementation of a graph that is represented as vertices and edges, which is used to perform topological sorting to yield the output of makefile.
 *HashMaps are used to match and generate the commands based on target dependencies. 
 */

import java.util.ArrayList;
import java.util.Stack;
import java.util.HashMap;

public class Builder
{
	
	
	private ArrayList<Vertex> vlist = new ArrayList<>();                                      //ArrayList to store the vertices
	private ArrayList<Edge> elist = new ArrayList<>();                                        //ArrayList to store the corresponding edges
	HashMap<String,String> edges = new HashMap<String,String>();                              //Hash map to store the commands and use them based on target dependencies
	
	private class Edge
	{
		String startVertex;
		String endVertex;
	}
	
	private class Vertex
	{
		int indeg;
		String value;
	}
	
	private void insertEdge(String V1, String V2)
	{
		Edge E=new Edge();
		E.startVertex=V1;
		E.endVertex=V2;
		elist.add(E);
	}
	
	private void insertVertex(String vertex)
	{
		boolean flag=true;
		Vertex V = new Vertex();
		V.value=vertex;
		V.indeg=0;
		for(Vertex vertx:vlist)
		{
			if(vertx.value.equals(vertex))
			{
				flag=false;
				break;
			}
		}
		if(flag)
		{
			vlist.add(V);
		}
	}
	
	
	
	/**
	 * 
	 * @param makefile the incoming file
	 * @throws ParseException
	 * @throws UnknownTargetException
	 * @throws CycleDetectedException
	 */
	public Builder(String makefile) throws ParseException,
	UnknownTargetException, CycleDetectedException {
		if(makefile==null)
		{
			throw new ParseException();
		}
		int count=0;
		int x=0;
		String tokens[] = makefile.split("\n");
		int length = tokens.length;
		for(x=0; x<length; x++)
		{
			count=0;
			if(!tokens[x].equals(""))																			//This section of the code is to parse the input string for
			{                                                                                                   //symbols such as semicolon and throw exceptions if any occurs. 
				for(int y=0; y<tokens[x].length(); y++)                                                
				{
					if(tokens[x].charAt(y)==':')
					{
						count++;
					}
				}
				String newTokens[]=tokens[x].split(":");
				if(count==0)
					throw new ParseException();
				if(newTokens.length==1 && count==2)
					throw new ParseException();
				if(count>=3)
					throw new ParseException();
				for(int l=0;l<(tokens.length-1);l++)
				{
					for(int j=l+1;j<tokens.length;j++)
					{
						if(tokens[l].equals(tokens[j]))
						{
							throw new ParseException();
						}
					}
				}
				insertVertex(newTokens[0]);
				if(!newTokens[1].equals(""))
				{
					if(newTokens[1].contains(" "))
					{
						String dep[] = newTokens[1].split(" ");
						for(int y=0;y<dep.length;y++)
						{
							insertVertex(dep[y]);
							insertEdge(dep[y],newTokens[0]);
						}
					}
					else
					{
						insertVertex(newTokens[1]);
						insertEdge(newTokens[1], newTokens[0]);
					}
				}
				edges.put(newTokens[0],newTokens[2]);
			}
		}
		for(Vertex V : vlist)
		{
			if(!edges.containsKey(V.value))
			{
				throw new UnknownTargetException();
			}

		}
		for(Edge E : elist)
		{
			String v1=E.startVertex;
			String v2=E.endVertex;                                                        //This section of the code is to detect if a cycle exists in graph
			for(Edge Edge : elist)
			{
				String v3=Edge.startVertex;
				String v4=Edge.endVertex;
				if(v3.equals(v2) && v4.equals(v1))
				{
					throw new CycleDetectedException();
				}                                                                         
			}
		}
	}

	/**
	 * 
	 * @param targetName the target
	 * @return result the made target
	 */
	public ArrayList<String> makeTarget(String targetName) {
		ArrayList<String> MakeTarget=new ArrayList<String>();
			                                                              //This section of the code is to perform a topological sort after the parsed input.
		boolean isthere=false;                                                                 //The target is built and returned via makeTarget method.
		for(Vertex Vertex:vlist)
		{
			if(Vertex.value.equals(targetName))
			{
				isthere=true;
			}		
		}
		if(isthere==false)
			return MakeTarget;                                                               
		for(Vertex V : vlist)
		{
			int indeg=0;
			for(Edge Edge:elist)
			{
				String endvertex=Edge.endVertex;
				if(V.value.equals(endvertex))                                                 
				{
					indeg++;
				}
			}
			V.indeg=indeg;
		}
		
		Stack<String> vertexstack=new Stack<String>();
		
		for(Vertex V:vlist)
		{
			if(V.indeg==0)
			{
				vertexstack.push(V.value);                 
			}
		}
		
		while(!vertexstack.empty())
		{
			if(vertexstack.contains(targetName))
			{
				String command=edges.get(targetName);
				MakeTarget.add(command);
				return MakeTarget;
			}
			String Vertex=vertexstack.pop();
			MakeTarget.add(edges.get(Vertex));
		
			for(Edge E:elist)
			{
				String StartVertex=E.startVertex;
				if(StartVertex.equals(Vertex))
				{
				String EndVertex=E.endVertex;
				E=new Edge();
			
				for(Vertex vertx:vlist)
				{
				if(vertx.value.equals(EndVertex))
				{
				vertx.indeg--;
				if(vertx.indeg==0)
				{
				vertexstack.push(EndVertex);
				}
				break;
				}
				
				}
				
				}
			}
		
		}
		
		return MakeTarget;																										
	}
}
