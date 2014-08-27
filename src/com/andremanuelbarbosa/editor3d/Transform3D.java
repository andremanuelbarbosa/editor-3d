package com.andremanuelbarbosa.editor3d;

public class Transform3D
{
    private float matrix[][];
    
    public Transform3D()
    {
        matrix = new float[4][4];
        init();
    }
    
    private void init()
    {
        for(int i = 0 ; i < 4 ; i++)
            for(int j = 0 ; j < 4 ; j++)
                if(i == j) matrix[i][j] = 1f;
                else matrix[i][j] = 0f;
    }
    
    public void translate(float dx,float dy,float dz)
    {
        init();
        
        matrix[0][3] = dx;
        matrix[1][3] = dy;
        matrix[2][3] = dz;
    }
    
    public void scale(float sx,float sy,float sz)
    {
        init();
        
        matrix[0][0] = sx;
        matrix[1][1] = sy;
        matrix[2][2] = sz;
    }
    
    public void rotateX(double alpha)
    {
        float sin;
        float cos;
        
        init();
        
        alpha = alpha * Math.PI / 180;
        
        sin = (float) Math.sin(alpha);
        cos = (float) Math.cos(alpha);
        
        matrix[1][1] = cos;
        matrix[1][2] = -sin;
        matrix[2][1] = sin;
        matrix[2][2] = cos;
    }
    
    public void rotateY(double alpha)
    {
        float sin;
        float cos;
        
        init();
        
        alpha = alpha * Math.PI / 180;
        
        sin = (float) Math.sin(alpha);
        cos = (float) Math.cos(alpha);
        
        matrix[0][0] = cos;
        matrix[0][2] = -sin;
        matrix[2][0] = sin;
        matrix[2][2] = cos;
    }
    
    public void rotateZ(double alpha)
    {
        float sin;
        float cos;
        
        init();
        
        alpha = alpha * Math.PI / 180;
        
        sin = (float) Math.sin(alpha);
        cos = (float) Math.cos(alpha);
        
        matrix[0][0] = cos;
        matrix[0][1] = -sin;
        matrix[1][0] = sin;
        matrix[1][1] = cos;
    }
    
    public float[][] matrix()
    {
        return matrix;
    }
    
    public void multiply(Transform3D t3D)
    {
        float t3DMatrix[][] = t3D.matrix();
        float f;
        
        for(int i = 0 ; i < 4 ; i++)
            for(int j = 0 ; j < 4 ; j++)
            {
                f = 0f;
                
                for(int k = 0 ; k < 4 ; k++)
                    f = f + matrix[i][k] * t3DMatrix[k][j];
                
                matrix[i][j] = f;
            }
    }
    
    public void multiply(Vertex v)
    {
        float x = matrix[0][0] * v.x + matrix[0][1] * v.y + matrix[0][2] * v.z + matrix[0][3] * 1;
        float y = matrix[1][0] * v.x + matrix[1][1] * v.y + matrix[1][2] * v.z + matrix[1][3] * 1;
        float z = matrix[2][0] * v.x + matrix[2][1] * v.y + matrix[2][2] * v.z + matrix[2][3] * 1;
        
        v.x = x;
        v.y = y;
        v.z = z;
    }
    
    public void invert()
    {
        float determinant = determinant();
        float[][] matrixAux = new float[4][4];

        matrixAux[0][0] = matrix[1][2] * matrix[2][3] * matrix[3][1] - matrix[1][3] * matrix[2][2] * matrix[3][1] + matrix[1][3] * matrix[2][1] * matrix[3][2] - matrix[1][1] * matrix[2][3] * matrix[3][2] - matrix[1][2] * matrix[2][1] * matrix[3][3] + matrix[1][1] * matrix[2][2] * matrix[3][3];
        matrixAux[0][1] = matrix[0][3] * matrix[2][2] * matrix[3][1] - matrix[0][2] * matrix[2][3] * matrix[3][1] - matrix[0][3] * matrix[2][1] * matrix[3][2] + matrix[0][1] * matrix[2][3] * matrix[3][2] + matrix[0][2] * matrix[2][1] * matrix[3][3] - matrix[0][1] * matrix[2][2] * matrix[3][3];
        matrixAux[0][2] = matrix[0][2] * matrix[1][3] * matrix[3][1] - matrix[0][3] * matrix[1][2] * matrix[3][1] + matrix[0][3] * matrix[1][1] * matrix[3][2] - matrix[0][1] * matrix[1][3] * matrix[3][2] - matrix[0][2] * matrix[1][1] * matrix[3][3] + matrix[0][1] * matrix[1][2] * matrix[3][3];
        matrixAux[0][3] = matrix[0][3] * matrix[1][2] * matrix[2][1] - matrix[0][2] * matrix[1][3] * matrix[2][1] - matrix[0][3] * matrix[1][1] * matrix[2][2] + matrix[0][1] * matrix[1][3] * matrix[2][2] + matrix[0][2] * matrix[1][1] * matrix[2][3] - matrix[0][1] * matrix[1][2] * matrix[2][3];
        matrixAux[1][0] = matrix[1][3] * matrix[2][2] * matrix[3][0] - matrix[1][2] * matrix[2][3] * matrix[3][0] - matrix[1][3] * matrix[2][0] * matrix[3][2] + matrix[1][0] * matrix[2][3] * matrix[3][2] + matrix[1][2] * matrix[2][0] * matrix[3][3] - matrix[1][0] * matrix[2][2] * matrix[3][3];
        matrixAux[1][1] = matrix[0][2] * matrix[2][3] * matrix[3][0] - matrix[0][3] * matrix[2][2] * matrix[3][0] + matrix[0][3] * matrix[2][0] * matrix[3][2] - matrix[0][0] * matrix[2][3] * matrix[3][2] - matrix[0][2] * matrix[2][0] * matrix[3][3] + matrix[0][0] * matrix[2][2] * matrix[3][3];
        matrixAux[1][2] = matrix[0][3] * matrix[1][2] * matrix[3][0] - matrix[0][2] * matrix[1][3] * matrix[3][0] - matrix[0][3] * matrix[1][0] * matrix[3][2] + matrix[0][0] * matrix[1][3] * matrix[3][2] + matrix[0][2] * matrix[1][0] * matrix[3][3] - matrix[0][0] * matrix[1][2] * matrix[3][3];
        matrixAux[1][3] = matrix[0][2] * matrix[1][3] * matrix[2][0] - matrix[0][3] * matrix[1][2] * matrix[2][0] + matrix[0][3] * matrix[1][0] * matrix[2][2] - matrix[0][0] * matrix[1][3] * matrix[2][2] - matrix[0][2] * matrix[1][0] * matrix[2][3] + matrix[0][0] * matrix[1][2] * matrix[2][3];
        matrixAux[2][0] = matrix[1][1] * matrix[2][3] * matrix[3][0] - matrix[1][3] * matrix[2][1] * matrix[3][0] + matrix[1][3] * matrix[2][0] * matrix[3][1] - matrix[1][0] * matrix[2][3] * matrix[3][1] - matrix[1][1] * matrix[2][0] * matrix[3][3] + matrix[1][0] * matrix[2][1] * matrix[3][3];
        matrixAux[2][1] = matrix[0][3] * matrix[2][1] * matrix[3][0] - matrix[0][1] * matrix[2][3] * matrix[3][0] - matrix[0][3] * matrix[2][0] * matrix[3][1] + matrix[0][0] * matrix[2][3] * matrix[3][1] + matrix[0][1] * matrix[2][0] * matrix[3][3] - matrix[0][0] * matrix[2][1] * matrix[3][3];
        matrixAux[2][2] = matrix[0][1] * matrix[1][3] * matrix[3][0] - matrix[0][3] * matrix[1][1] * matrix[3][0] + matrix[0][3] * matrix[1][0] * matrix[3][1] - matrix[0][0] * matrix[1][3] * matrix[3][1] - matrix[0][1] * matrix[1][0] * matrix[3][3] + matrix[0][0] * matrix[1][1] * matrix[3][3];
        matrixAux[2][3] = matrix[0][3] * matrix[1][1] * matrix[2][0] - matrix[0][1] * matrix[1][3] * matrix[2][0] - matrix[0][3] * matrix[1][0] * matrix[2][1] + matrix[0][0] * matrix[1][3] * matrix[2][1] + matrix[0][1] * matrix[1][0] * matrix[2][3] - matrix[0][0] * matrix[1][1] * matrix[2][3];
        matrixAux[3][0] = matrix[1][2] * matrix[2][1] * matrix[3][0] - matrix[1][1] * matrix[2][2] * matrix[3][0] - matrix[1][2] * matrix[2][0] * matrix[3][1] + matrix[1][0] * matrix[2][2] * matrix[3][1] + matrix[1][1] * matrix[2][0] * matrix[3][2] - matrix[1][0] * matrix[2][1] * matrix[3][2];
        matrixAux[3][1] = matrix[0][1] * matrix[2][2] * matrix[3][0] - matrix[0][2] * matrix[2][1] * matrix[3][0] + matrix[0][2] * matrix[2][0] * matrix[3][1] - matrix[0][0] * matrix[2][2] * matrix[3][1] - matrix[0][1] * matrix[2][0] * matrix[3][2] + matrix[0][0] * matrix[2][1] * matrix[3][2];
        matrixAux[3][2] = matrix[0][2] * matrix[1][1] * matrix[3][0] - matrix[0][1] * matrix[1][2] * matrix[3][0] - matrix[0][2] * matrix[1][0] * matrix[3][1] + matrix[0][0] * matrix[1][2] * matrix[3][1] + matrix[0][1] * matrix[1][0] * matrix[3][2] - matrix[0][0] * matrix[1][1] * matrix[3][2];
        matrixAux[3][3] = matrix[0][1] * matrix[1][2] * matrix[2][0] - matrix[0][2] * matrix[1][1] * matrix[2][0] + matrix[0][2] * matrix[1][0] * matrix[2][1] - matrix[0][0] * matrix[1][2] * matrix[2][1] - matrix[0][1] * matrix[1][0] * matrix[2][2] + matrix[0][0] * matrix[1][1] * matrix[2][2];
        
        double d = 1 / determinant;
        
        for(int i = 0 ; i < 4 ; i++)
            for(int j = 0 ; j < 4 ; j++)
                matrixAux[i][j] *= d;
        
        matrix = matrixAux;
    }
    
    private float determinant()
    {
        float determinant;
        
        float f1 = matrix[0][3] * matrix[1][2] * matrix[2][1] * matrix[3][0] - matrix[0][2] * matrix[1][3] * matrix[2][1] * matrix[3][0] - matrix[0][3] * matrix[1][1] * matrix[2][2] * matrix[3][0] + matrix[0][1] * matrix[1][3] * matrix[2][2] * matrix[3][0];
        float f2 = matrix[0][2] * matrix[1][1] * matrix[2][3] * matrix[3][0] - matrix[0][1] * matrix[1][2] * matrix[2][3] * matrix[3][0] - matrix[0][3] * matrix[1][2] * matrix[2][0] * matrix[3][1] + matrix[0][2] * matrix[1][3] * matrix[2][0] * matrix[3][1];
        float f3 = matrix[0][3] * matrix[1][0] * matrix[2][2] * matrix[3][1] - matrix[0][0] * matrix[1][3] * matrix[2][2] * matrix[3][1] - matrix[0][2] * matrix[1][0] * matrix[2][3] * matrix[3][1] + matrix[0][0] * matrix[1][2] * matrix[2][3] * matrix[3][1];
        float f4 = matrix[0][3] * matrix[1][1] * matrix[2][0] * matrix[3][2] - matrix[0][1] * matrix[1][3] * matrix[2][0] * matrix[3][2] - matrix[0][3] * matrix[1][0] * matrix[2][1] * matrix[3][2] + matrix[0][0] * matrix[1][3] * matrix[2][1] * matrix[3][2];
        float f5 = matrix[0][1] * matrix[1][0] * matrix[2][3] * matrix[3][2] - matrix[0][0] * matrix[1][1] * matrix[2][3] * matrix[3][2] - matrix[0][2] * matrix[1][1] * matrix[2][0] * matrix[3][3] + matrix[0][1] * matrix[1][2] * matrix[2][0] * matrix[3][3];
        float f6 = matrix[0][2] * matrix[1][0] * matrix[2][1] * matrix[3][3] - matrix[0][0] * matrix[1][2] * matrix[2][1] * matrix[3][3] - matrix[0][1] * matrix[1][0] * matrix[2][2] * matrix[3][3] + matrix[0][0] * matrix[1][1] * matrix[2][2] * matrix[3][3];
        
        determinant = f1 + f2 + f3 + f4 + f5 + f6;
        
        return determinant;
    }
}