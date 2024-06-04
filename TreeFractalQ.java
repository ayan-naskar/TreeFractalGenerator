import java.awt.Graphics;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Stroke;
import java.util.LinkedList;
import java.util.Queue;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


public class TreeFractalQ extends Frame {
    static double initialLen,stroke,startingAngle;
    static int depth,currDepth,counting=0;
    static double c=0.0,pinkoffset;
    static int xoffset; 
    static boolean colorselect; // type of tree including the bending factor
    static float thickness;
    class Branch{
        double x,y,p,q,stroke,angle;

        public Branch(double x, double y, double p, double q, double stroke, double angle, Graphics g) {
            this.x = x;
            this.y = y;
            this.p = p;
            this.q = q;
            this.stroke = stroke;
            this.angle = angle;
            print(g);
        }        
        void print(Graphics gr){
            Graphics2D g = (Graphics2D)gr;
            Stroke sstroke = new BasicStroke((float)(thickness*10*(1-Math.log(depth==currDepth?0.5:depth-currDepth)/Math.log(depth))));
            g.setStroke(sstroke);
            g.setColor(new Color((int)Math.max(stroke,0),(int)(Math.min(240,stroke*1.20)),(int)Math.max(0,stroke)));
            if(currDepth-depth<=-9  && colorselect){//&& Math.random()>0.95){
                g.setColor(new Color((int)(Math.min(240,stroke*1.20)),(int)(Math.max(stroke,0)*pinkoffset),(int)(Math.max(0,stroke)*pinkoffset)));
                // Color color = getShadesOfPink();
                // sstroke = new BasicStroke(2f);
                // g.setStroke(sstroke);
                // g.setColor(color);
            }
            int cx=(int)x+xoffset,cy=1080-((int)y+20), ccx = (int)p+xoffset,ccy=1080-((int)q+20); 
            g.drawLine(cx,cy,ccx,ccy);
            // try{Thread.sleep(3);}
            // catch(InterruptedException e){e.printStackTrace();}
        }

        Color getShadesOfPink(){
            // int r=255-(int)((Math.random())*50),g=r-(int)((Math.random())*100);
            // int gg=Math.min(255,g+25);
            // int b=gg-(int)((Math.random())*50);
            // r=g=b=0;
            // r=Math.max(255,r); r=Math.max(255,r); b=Math.max(255,b);
            // return new Color(r,g,b);
            return new Color(255,100,100);
            // c+=0.001;
            // if(c==1) c=0.0;
            // if(c<0.25) return new Color(0,0,0);
            // else if(c<0.5)
            //     return new Color (252, 108, 133);
            // else return new Color(255, 193, 204);
        }

        void printFlower(Graphics g){
            g.setColor(new Color(255,Math.random()>0.5?255:0,0));
            int len;
            g.fillOval((int)x+610, 1000-((int)y+20), len=(int)(5*Math.random()), len);
        }
    }



    public static void main(String[] args) {
        new TreeFractalQ();
        // new TreeFractalQ();
    }
    TreeFractalQ(){
        this.setSize(1920,1080);
        this.setVisible(true);
        this.setBackground(new Color(160,160,160));
    }
    void initialise(){
        depth=(int)(Math.random()*4+16);
        stroke=255;//here i am using stroke as color, because i cudnt do stroke in line
        startingAngle=90.0;
        initialLen=150.0*Math.random()+50;
        xoffset=960+(int)((Math.random()*2.0-1.0)*960);
        pinkoffset=0.75+Math.random()*0.25;
        if(Math.random()>0.75) colorselect=true;
        else colorselect=false;
        thickness = (float)initialLen/200.0f;
    }
    public void paint(Graphics gr){
        try{Thread.sleep(0);}
            catch(InterruptedException e){e.printStackTrace();}
        if(counting++<2)return;
        Graphics2D g = (Graphics2D) gr;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(0,0,0));
        // boolean flag=true;
        Branch root = null;
        for(int j=0;j<5;j+=0){
            initialise();
            currDepth=depth;
            
            Queue<Branch> q= new LinkedList<>();
            root = new Branch((Math.random()-0.5)*100,0.0,0.0,initialLen,0.0,startingAngle,g);
            q.add(root);
            for(int i=0;i<=depth;i++){
                Queue<Branch> pq = new LinkedList<>();
                while(!q.isEmpty()){
                    Branch br = q.poll();
                    Queue<Branch> qq = recurse(g,br,depth-i);
                    pq.addAll(qq);
                }
                q=pq;
                try{Thread.sleep(200);}
                catch(InterruptedException e){e.printStackTrace();}
            }
        }
        // System.out.println(counting);
    }
    Queue<Branch> recurse(Graphics g, Branch branch, int depth){
        Queue<Branch> q= new LinkedList<>();

        if(depth==0){
            // if(Math.random()>0.95)
            //     branch.printFlower(g); 
            return q;
        }
        // if(depth>10 && Math.random()>0.55) branch.printFlower(g);
        double angleL=branch.angle-Math.random()*40.0,angleR=branch.angle+Math.random()*40.0;

        double len=distance(branch.x,branch.y,branch.p,branch.q)/1.25, base;
        if(angleL<0 && colorselect){
            angleL=180.0;
            // len*=0.75;
        }

        double x=(base=Math.cos(Math.toRadians(angleL))*len)+branch.p,y=Math.tan(Math.toRadians(angleL))*base+branch.q;
        
        double randomLenProb=Math.random();
        
        currDepth=depth;
        if(randomLenProb>0.5){
            Branch bL = new Branch(branch.p,branch.q,x,y,stroke-255*(depth/(double)TreeFractalQ.depth),angleL,g);
            // recurse(g,bL,depth-1);
            q.add(bL);
        }else{
            x=(base=Math.cos(Math.toRadians(angleL))*len*Math.max(Math.random(),0.65))+branch.p; y=Math.tan(Math.toRadians(angleL))*base+branch.q;
            Branch bL = new Branch(branch.p,branch.q,x,y,stroke-255*(depth/(double)TreeFractalQ.depth),angleL,g);
            // recurse(g,bL,depth-1);
            q.add(bL);
        }

        if(Math.random()>0.85){
            double angleM=branch.angle+((Math.random()-0.5)*30.0);
            x=(base=Math.cos(Math.toRadians(angleM))*len)+branch.p; y=Math.tan(Math.toRadians(angleM))*base+branch.q;

            currDepth=depth;
            Branch bM = new Branch(branch.p,branch.q,x,y,stroke-255*(depth/(double)TreeFractalQ.depth),angleM,g);
            // recurse(g,bM,depth-1);
            q.add(bM);
        }

        if(angleR>180 && colorselect){
            angleR=0;
            // len*=0.75;
        }

        currDepth=depth;
        if(randomLenProb>0.5){
            x=(base=Math.cos(Math.toRadians(angleR))*len*Math.max(Math.random(),0.65))+branch.p; y=Math.tan(Math.toRadians(angleR))*base+branch.q;
            Branch bR = new Branch(branch.p,branch.q,x,y,stroke-255*(depth/(double)TreeFractalQ.depth),angleR,g);
            // recurse(g,bR,depth-1);
            q.add(bR);
        }else{
            x=(base=Math.cos(Math.toRadians(angleR))*len)+branch.p; y=Math.tan(Math.toRadians(angleR))*base+branch.q;
            Branch bR = new Branch(branch.p,branch.q,x,y,stroke-255*(depth/(double)TreeFractalQ.depth),angleR,g);
            // recurse(g,bR,depth-1);
            q.add(bR);
        }
        return q;
    }
    double distance(double x, double y, double p, double q){
        return Math.sqrt(Math.pow(p-x,2)+Math.pow(q-y,2));
    }
}
