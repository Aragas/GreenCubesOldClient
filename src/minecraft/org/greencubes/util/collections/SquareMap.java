/*
 * Copyright 2015 Eiren 'Eirenliel' Rain and GreenCubes.org
 * authors
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall
 * be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.greencubes.util.collections;

/**
 * ��������� Thread-Safe ����� ��� �������� ������ � ���� "(x,z)-����������;�������"
 * @author Rena (anya.a.net@gmail.com)
 *
 * @param <E> - ��� �������� ��������
 */

public final class SquareMap<E> {
	
	private final int startX;
	private final int startZ;
	private final int sizeX;
	private final int sizeZ;
	private final Object[] map;
	private final boolean[] state;
	private int size = 0;
	private final Object locker = new Object();

	/**
	 * ������ ������ �����
	 * @param startX - ������ ��������� �� X
	 * @param startZ - ������ ��������� �� Z
	 * @param endX - ����� ��������� �� X
	 * @param endZ - ����� ��������� �� Z
	 */
	public SquareMap(int startX, int startZ, int endX, int endZ) {
		this.startX = startX;
		this.startZ = startZ;
		this.sizeX = endX - startX + 1;
		this.sizeZ = endZ - startZ + 1;
		map = new Object[sizeX * sizeZ];
		state = new boolean[sizeX * sizeZ];
	}
	
	/**
	 * ���������� ������, ���������� �� ����������� x, z
	 * @param x - ���������� X
	 * @param z - ���������� Z
	 * @return E - ������ �� �������� �����������
	 * @throws ArrayIndexOutOfBoundsException ���� X ��� Z �� ������ � ������� �����
	 */
	@SuppressWarnings("unchecked")
	public E get(int x, int z) {
		checkBounds(x, z);
		x -= startX;
		z -= startZ;
		return (E) map[sizeZ * x + z];
	}
	
	/**
	 * ��������� �� �������� ����������� x � z ������
	 * @param x - ���������� X
	 * @param z - ���������� Z
	 * @param object - ������
	 * @throws ArrayIndexOutOfBoundsException ���� X ��� Z �� ������ � ������� �����
	 */
	public void set(int x, int z, E object) {
		checkBounds(x, z);
		x -= startX;
		z -= startZ;
		synchronized(locker) {
			map[sizeZ * x + z] = object;
			if(object != null && !state[sizeZ * x + z]) {
				state[sizeZ * x + z] = true;
				size++;
			} else if(object == null && state[sizeZ * x + z]) {
				size--;
				state[sizeZ * x + z] = false;
			}
		}
	}
	
	/**
	 * ����� � set
	 * @see set
	 */
	public void put(int x, int z, E object) {
		set(x, z, object);
	}
	
	/**
	 * ������� �����. <b>� �������� �����������, ����� ����� �� ����������,
	 * ���� �� ����� ������� ����������� ������ � ������ �����
	 */
	public void clear() {
		for(int i = 0; i < map.length; ++i) {
			synchronized(locker) {
				if(state[i]) {
					map[i] = null;
					state[i] = false;
					--size;
				}
			}
		}
	}
	
	/**
	 * ������� ������� �� ����� �� �����������
	 * @param x - ���������� X
	 * @param z - ���������� Z
	 */
	public void remove(int x, int z) {
		checkBounds(x, z);
		x -= startX;
		z -= startZ;
		synchronized(locker) {
			if(state[sizeZ * x + z]) {
				map[sizeZ * x + z] = null;
				size--;
				state[sizeZ * x + z] = false;
			}
		}
	}
	
	private final void checkBounds(int x, int z) {
		if(x < startX || z < startZ || x >= startX + sizeX || z >= startZ + sizeZ)
			throw new ArrayIndexOutOfBoundsException("Index: " + x + ", " + z);
	}
	
	/**
	 * ���������� ������ �����
	 * @return ������ �����
	 */
	public final int size() {
		return size;
	}
	
	/**
	 * ���������, ���� ��� ��� ������ �� �����������
	 * @param x - ���������� X
	 * @param z - ���������� Z
	 * @return boolean ������� ������� �� �����������
	 */
	public boolean isEmpty(int x, int z) {
		checkBounds(x, z);
		x -= startX;
		z -= startZ;
		return !state[sizeZ * x + z];
	}
	
	/**
	 * Метод очень неэффективен! Желательно отказаться от использования
	 * @param val
	 * @return
	 */
	public boolean containsValue(E val) {
		if(val == null)
			return true;
		for(int i = 0; i < map.length; ++i)
			if(state[i] == true && map[i] != null && map[i].equals(val))
				return true;
		return false;
	}
	
	/**
	 * ���������� ������ ���� ��������� �����.<br />
	 * <b>��������!</b> ������ ����� ���� �������� � �������� ����������� ��� ��������� null!
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public E[] values() {
		Object[] ret = new Object[size];
		int size = 0;
		for(int x = 0; x < sizeX; ++x)
			for(int z = 0; z < sizeZ; ++z) {
				if(size == ret.length)
					break;
				if(state[sizeZ * x + z])
					ret[size++] = map[sizeZ * x + z];
			}
		if(size < ret.length) {
			Object[] ret2 = new Object[size];
			System.arraycopy(ret, 0, ret2, 0, size);
			return (E[]) ret2;
		}
		return (E[]) ret;
	}
	
	public boolean containsFullRegion(int x1, int z1, int x2, int z2) {
		x1 -= startX;
		z1 -= startZ;
		x2 -= startX;
		z2 -= startZ;
		if(x1 < 0 || x2 < 0 || z1 < 0 || z2 < 0 || x1 >= sizeX || x2 >= sizeX || z1 >= sizeZ || z2 >= sizeZ)
			return false;
		for(int x = x1; x <= x2; ++x)
			for(int z = z1; z <= z2; ++z)
				if(!state[sizeZ * x + z])
					return false;
		return true;
	}
	
	/**
	 * ���������� ������ ���������, ���������� �������<br />
	 * <b>������ ����� � ����� ����� �������� ������������!</b>
	 * @return CoordinatesArray
	 */
	public CoordinatesArray getCoordinates() {
		int[] xs = new int[size];
		int[] zs = new int[size];
		int size = 0;
		for(int x = 0; x < sizeX; ++x)
			for(int z = 0; z < sizeZ; ++z) {
				if(size == xs.length)
					break;
				if(state[sizeZ * x + z]) {
					xs[size] = x + startX;
					zs[size++] = z + startZ;
				}
			}
		if(size < xs.length) {
			int[] newXs = new int[size];
			int[] newZs = new int[size];
			System.arraycopy(xs, 0, newXs, 0, size);
			System.arraycopy(zs, 0, newZs, 0, size);
			return new CoordinatesArray(newXs, newZs);
		}
		return new CoordinatesArray(xs, zs);
	}
	
	public static final class CoordinatesArray {
		
		private final int size;
		private final int[] xs;
		private final int[] zs;
		
		public CoordinatesArray(int[] xs, int[] zs) {
			if(xs.length != zs.length)
				throw new IllegalArgumentException("X's array size != Z's array size");
			this.xs = xs;
			this.zs = zs;
			this.size = xs.length;
		}
		
		public int getX(int index) {
			checkBounds(index);
			return xs[index];
		}
		
		public int getZ(int index) {
			checkBounds(index);
			return zs[index];
		}
		
		private void checkBounds(int index) {
			if(index < 0 || index >= size)
				throw new ArrayIndexOutOfBoundsException("Size: " + size + ", index: " + index);
		}

		public int size() {
			return size;
		}
	}
}
