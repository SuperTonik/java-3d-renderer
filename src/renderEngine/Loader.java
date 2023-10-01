package renderEngine;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import models.RawModel;
import texture.Texture;

public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>();		// List of all created VAO's
	private List<Integer> vbos = new ArrayList<Integer>();		// List of all created VBO's
	private List<Integer> textures = new ArrayList<Integer>();	// List of all texture ID's.
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public int loadTexture(String fileName) {
		Texture texture;
		try {
			texture = new Texture("res/" + fileName + ".png");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	/*
	 *  Method to create a new VAO.
	 */
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays(); 	// Creates empty VAO.
		vaos.add(vaoID);						// Add it to the list of all VAO's.
		GL30.glBindVertexArray(vaoID); 			// Activates the VAO.
		return vaoID;
	}
	
	/*
	 *  Method to create VBO's and store them into attribute list.
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();					// Create new VBO.
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); 	// Bind VBO to use it.
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); 	// Unbind.
	}
	
	private void unbindVAO() {
		GL30.glBindVertexArray(0);	// Unbinds the currently bound VAO.
	}
	
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	/*
	 *  Convert vertex data to a float buffer.
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();			// Buffer must be flipped before it can be read!
		return buffer;
	}
	
	/*
	 *  Delete used VAO's and VBO's after program is finished.
	 */
	public void cleanUp() {
		for (int vao: vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo: vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture: textures) {
			GL11.glDeleteTextures(texture);
		}
	}
}
