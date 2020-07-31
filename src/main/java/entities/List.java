package entities;

import java.util.Comparator;
import java.util.Iterator;

public class List<T> implements Iterable<T> {		//Generalized List type with Iterable interface implemented
	private int size;				//Size of the list
	private Object[] elements;		//Array based list
	private Comparator<T> comp;		//Comparator to sort by

	public List(Comparator<T> comp){	//Initialize the list object with the given comparator
		this.size = 0;
		this.elements = new Object[1];	//Create a new object arary of size 1
		this.comp = comp;
	}

	public void clear() {				//Method to clear the arraylist
		this.size = 0;					//Set size to 0
		this.elements = new Object[1];	//Create a new object array of size 1
	}

	public int size() {					//Useful method to get size
		return size;
	}

	private boolean isEmpty() {			//Method to check if the list is empty (boolean type)
		if(this.size == 0 || this.elements == null) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean add(T t) {			//Method to add the given item to the list (order maintained upon insertion)
		if(t == null){
			throw new NullPointerException("Cannot add a null value to the list.");	//If the provided value is null
		}
		else{
			Object[] temp = elements;					//Store the current elements in a new temp array
			elements = new Object[elements.length+1];	//Create a new array with 1 more size than before
			if(this.isEmpty()) {						
				elements[0] = t;						//If it's empty, then add the element directly and increment size
				size++;
			}
			else {
				//Add all the current elements to the new array
				if (size >= 0) System.arraycopy(temp, 0, elements, 0, size);
				
				if(this.comp.compare(t, (T) elements[size-1]) < 0) {		//Insert the element at the end depending on the sort type
					elements[size] = t;
				}
				else {														
					for(int i = 0; i < size; i++){								//Loop through each of the element in the new array
						int result = this.comp.compare(t, (T) elements[i]);	//Store the result of the comparator
						if (result >= 0) {						//Else..
							elements[i] = t;		//Store the provided element at the given position
							//Add the rest of the element after that position
							System.arraycopy(temp, i, elements, i + 1, size - i);
							break;		//Break
						}  //Continue if doesn't satisfy the result (< 0)

					}
				}
				size++;			//Increment the size
			}
		}
		return true;		//If successfully added, return true
	}

	private void remove(int position) {
		if (position < 0 || position >= size){
			throw new IndexOutOfBoundsException("Invalid position to remove.");
		}
		else{
			Object[] temp = elements;
			elements = new Object[size-1];
			System.arraycopy(temp, 0, elements, 0, position);
			if (size - 1 - position >= 0) System.arraycopy(temp, position + 1, elements, position, size - 1 - position);
			size--;
		}
	}

	public boolean contains(Object element) {		//Check if the list contains the given value
		if(element == null) {						//If the provided value to compare is null..
			throw new NullPointerException("Cannot compare a null value.");
		}
		else {
			for(Object e : this.elements) {		//Loop through each objects
				if(element.equals(e)) {			//Check if those two objects equal
					return true;				//If they do, return true
				}
			}
		}
		return false;							//Else false
	}


	@SuppressWarnings("unchecked")
	public T get(int position) {					//Get the value at the given position
		if (position < 0 || position > size){		//If the position is out of bound
			throw new IndexOutOfBoundsException("Invalid position to retreive.");
		}
		return (T) elements[position];				//Else return the object at that given position
	}

	public Comparator<T> getComp(){					//Method to get the comparator
		return this.comp;
	}

	public void print() {							//Method to print the list
		for(int i = 0; i < size; i++){
			System.out.println(elements[i].toString());
		}
	}


	@Override
	public Iterator<T> iterator() {					//Overridden iterator to implement iterable interface
		return new Iterator<T>() {		//Create new iterable object

			private int currentIndex = 0;

			@Override
			public boolean hasNext() {
				return currentIndex < size && elements[currentIndex] != null;	//Check if the list has more element
			}

			@SuppressWarnings("unchecked")
			@Override
			public T next() {
				return (T) elements[currentIndex++];	//Get the next element of the list
			}

			@Override
			public void remove() {
				List.this.remove(currentIndex);		//Remove from the given index
			}
		};									//Return the iterable object
	}
}
