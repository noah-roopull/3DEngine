import java.util.ArrayList;

public class Maze {
  public int w,h;
  public int[][] cells;
  public Maze(int w,int h,boolean braid) { //if braid, carve another wall to avoid dead ends - more consistent
    this.w=w;
    this.h=h;
    int ch=h*2+1;
    int cw=w*2+1;
    boolean[][] visited=new boolean[h][w];
    this.cells=new int[ch][cw];
    for (int i=0;i<ch;i++) {
      for (int j=0;j<cw;j++) {
        cells[i][j]=1;
      }
    }
    cells[1][1]=0;
    visited[0][0]=true;
    ArrayList<int[]> runners=new ArrayList<int[]>();
    runners.add(new int[]{0,0});
    int[] lastdir=new int[]{0,0};
    while (runners.size()>0) {
      int[] r=runners.remove((int)Math.floor(Math.random()*runners.size()));
      ArrayList<int[]> targets=new ArrayList<int[]>();
      ArrayList<int[]> btargets=new ArrayList<int[]>();
      if (r[0]<w-1) {
        if (!visited[r[1]][r[0]+1]) {
          targets.add(new int[]{1,0});
        } else {
          btargets.add(new int[]{1,0});
        }
      }
      if (r[1]<h-1) {
        if (!visited[r[1]+1][r[0]]) {
          targets.add(new int[]{0,1});
        } else {
          btargets.add(new int[]{0,1});
        }
      }
      if (r[0]>0) {
        if (!visited[r[1]][r[0]-1]) {
          targets.add(new int[]{-1,0});
        } else {
          btargets.add(new int[]{-1,0});
        }
      }
      if (r[1]>0) {
        if (!visited[r[1]-1][r[0]]) {
          targets.add(new int[]{0,-1});
        } else {
          btargets.add(new int[]{0,-1});
        }
      }
      if (targets.size()>1) {
        runners.add(r);
      }
      if (targets.size()>0) {
        lastdir=targets.get((int)Math.floor(Math.random()*targets.size()));
        runners.add(new int[]{r[0]+lastdir[0],r[1]+lastdir[1]});
      } else {
        if (braid) {
          int[] braiddir=btargets.get((int)Math.floor(Math.random()*btargets.size()));
          if (braiddir[0]==-lastdir[0]&&braiddir[1]==-lastdir[1]) {
            btargets.remove(braiddir);
            lastdir=btargets.get((int)Math.floor(Math.random()*btargets.size()));
          } else {
            lastdir=braiddir;
          }
        } else {
          continue;
        }
      }
      this.cells[r[1]*2+1+lastdir[1]][r[0]*2+1+lastdir[0]]=0; //carve pathway
      this.cells[(r[1]+lastdir[1])*2+1][(r[0]+lastdir[0])*2+1]=0; //clear actual target
      visited[r[1]+lastdir[1]][r[0]+lastdir[0]]=true; //mark as visited to avoid returning later
    }
  }
  public String toString() {
    String s="";
    for (int i=0;i<this.h*2+1;i++) {
      for (int j=0;j<this.w*2+1;j++) {
        if (this.cells[i][j]>0) {
          String x="0123456789ABCDEFGHJKLMNOPQRSTUVWXYZ".substring(this.cells[i][j],this.cells[i][j]+1);
          s+=x+x; //██ if all cells are 1
        } else {
          s+="  ";
        }
      }
      s+="\n";
    }
    return s;
  }
}