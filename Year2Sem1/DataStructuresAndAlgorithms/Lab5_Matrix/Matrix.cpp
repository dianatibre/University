#include "Matrix.h"
#include <exception>
#include <iostream>
using namespace std;


Matrix::Matrix(int nrLines, int nrCols) {
	//TODO - Implementation
    this->nLines=nrLines;
    this->nCols=nrCols;
    this->size=0;
    this->firstEmpty=0;
    this->m=100;
    this->nodes= new Node[this->m];
    for (int i=0; i<this->m; i++) {
       // this->nodes->info=-1;
        this->nodes[i].next=-1;
        this->nodes[i].info.value=NULL_TELEM;
        this->nodes[i].info.line=-1;
        this->nodes[i].info.column=-1;
    }
}

TFunction Matrix::h(const int i, const int j) const {
    //return (i+j)%this->m;
    //return (i*i+j*j)%this->m;
    
    //Complexity: Theta(1)
    return (i*j)%this->m;
}

int Matrix::nrLines() const {
	//TODO - Implementation
    //Theta(1)
	return this->nLines;
}


int Matrix::nrColumns() const {
	//TODO - Implementation
    //Theta(1)
	return this->nCols;
}


TElem Matrix::element(int i, int j) const {
	//TODO - Implementation
    //Complexity: O(n)
    //BC: O(1)
    //WC: O(n)
    //AC: O(n)
    if (i>this->nLines or j>this->nCols or i<0 or j<0)
        throw exception();
    if (h(i,j)<0 or h(i,j)>m)
        throw exception();
    
    int pos=h(i,j);
    //cout << h(i,j) << endl;
    //while (pos!=-1 && this->nodes[pos].info.value!=NULL_TELEM && (this->nodes[pos].info.line!=i || this->nodes[pos].info.column!=j)) {
    if (this->nodes[pos].info.line == -1) { //position is empty
                return NULL_TELEM;
        }
    while (pos!=-1 && (this->nodes[pos].info.line!=i || this->nodes[pos].info.column!=j)) {
        pos=this->nodes[pos].next;
    }
    if (pos!=-1 && this->nodes[pos].info.line==i && this->nodes[pos].info.column==j) {
        return this->nodes[pos].info.value;
    }
    else
        return NULL_TELEM;
    /*
    while (pos!=-1) {
        if (this->nodes[pos].info.line==i && this->nodes[pos].info.column==j)
            return this->nodes[pos].info.value;
        pos=this->nodes[pos].next;
    }
    return NULL_TELEM;
    */
    
}

TElem Matrix::remove(int i, int j, TElem e) {
    //Complexity:
    //BC: O(1)
    //WC: O(n)
    //AC: O(n)
    int pos=h(i,j);
    int prevpos=-1;
    int p,pp;
    int previousValue=element(i, j);
    //find the line and column
    if (this->nodes[pos].info.line == -1) { //position is empty
                return NULL_TELEM;
        }
    while (pos!=-1 && (this->nodes[pos].info.line!=i || this->nodes[pos].info.column!=j)) {
        prevpos=pos;
        pos=this->nodes[pos].next;
    }
    if (pos==-1 || previousValue==NULL_TELEM) {
        return NULL_TELEM;
    }
    else {
        bool over=false;
        do {
            p=this->nodes[pos].next;
            pp=pos;
            while (p!=-1 && h(this->nodes[p].info.line,this->nodes[p].info.column)!=pos) {
                pp=p;
                p=this->nodes[p].next;
            }
            if (p==-1) {
                over=true;
            }
            else {
                //this->nodes[pos]=this->nodes[p];
                this->nodes[pos].info=this->nodes[p].info;
                pos=p;
                prevpos=pp;
            }
            //cout << pos << endl;
        } while (!over);
        if (prevpos==-1) {
            int index=0;
            //to set prevpos correctly
            while (index<this->m && prevpos==-1) {
                if (this->nodes[index].next==pos) {
                    prevpos=index;
                }
                index++;
            }
        }
        if (prevpos!=-1) {
            this->nodes[prevpos].next=this->nodes[pos].next;
        }
        this->nodes[pos].next=-1;
        this->nodes[pos].info.value=NULL_TELEM;
        this->nodes[pos].info.line=-1;
        if (pos<this->firstEmpty) {
            this->firstEmpty=pos;
        }
        return previousValue;
    }
}

TElem Matrix::modify(int i, int j, TElem e) {
	//TODO - Implementation
    //Complexity:
    //BC: O(1)
    //WC: O(n)
    //AC: O(n)
    if (i>this->nLines or j>this->nCols or i<0 or j<0)
        throw exception();
    if (h(i,j)<0 or h(i,j)>m)
        throw exception();
    
    if (e==NULL_TELEM) {
        return remove(i, j, e);
    }
    
    Node* newNode = new Node;
    newNode->info.line=i;
    newNode->info.column=j;
    newNode->info.value=e;
    newNode->next=-1;
    if (element(i, j)==NULL_TELEM) {
        int pos=h(i,j);
        //if (this->nodes[pos].info.value==NULL_TELEM) {
        if (this->nodes[pos].info.line==-1) {
            this->nodes[pos].info=newNode->info;
            this->nodes[pos].next=-1;
            //delete newNode;
            if (pos==firstEmpty) {
                changeFirstEmpty();
            }
            return NULL_TELEM;
        }
        else {
            if (this->firstEmpty==this->m) {
                //resize and rehash
                rehashAndResize();
                pos=h(i,j);
                this->firstEmpty=-1;
                changeFirstEmpty();
            }
            int currentPos=pos;
            while (this->nodes[currentPos].next!=-1) {
                currentPos=this->nodes[currentPos].next;
            }
            this->nodes[this->firstEmpty].info=newNode->info;
            this->nodes[this->firstEmpty].next=-1;
            this->nodes[currentPos].next=this->firstEmpty;
            changeFirstEmpty();
            //delete newNode;
            return NULL_TELEM;
        }
    }
    else {
        int pos=h(i,j);
        while (pos!=-1 && (this->nodes[pos].info.line!=i || this->nodes[pos].info.column!=j)) {
            pos=this->nodes[pos].next;
        }
        if (pos!=-1 && this->nodes[pos].info.line==i && this->nodes[pos].info.column==j) {
            TElem prev = this->nodes[pos].info.value;
            this->nodes[pos].info.value=e;
            return prev;
        }
        
    }
	return NULL_TELEM;
}

void Matrix::changeFirstEmpty() {
    //Complexity: O(n)
    this->firstEmpty++;
    while (this->firstEmpty<this->m && this->nodes[this->firstEmpty].info.line!=-1){ //this->nodes[this->firstEmpty].info.value!=NULL_TELEM) {
        this->firstEmpty++;
    }
}

void Matrix::rehashAndResize() {
    //Complexity: O(n^2)
    this->m=this->m*2;
    //this->size=this->size+10;
    Node* newNodes = new Node[this->m];
    for (int i=0; i<this->m; i++) {
        newNodes[i].info.value=NULL_TELEM;
        newNodes[i].next=-1;
        newNodes[i].info.line=-1;
        newNodes[i].info.column=-1;
    }
    for (int i=0; i<this->m/2; i++) {
        if (this->nodes[i].info.line!=-1) { //if (this->nodes[i].info.value!=NULL_TELEM) {
            int hash=h(this->nodes[i].info.line,this->nodes[i].info.column);
            if (newNodes[hash].info.value==NULL_TELEM) {
                newNodes[hash].info=this->nodes[i].info;
            }
            else {
                int firstEmpty2=0;
                while (firstEmpty2<this->m && newNodes[firstEmpty2].info.line!=-1) {//newNodes[firstEmpty2].info.value!=NULL_TELEM) {
                    firstEmpty2++;
                }
                //if (newNodes[firstEmpty2].info.value==NULL_TELEM) {
                if (newNodes[firstEmpty2].info.line==-1) {
                    newNodes[firstEmpty2].info=this->nodes[i].info;
                    while (newNodes[hash].next!=-1) {
                        hash=newNodes[hash].next;
                    }
                    newNodes[hash].next=firstEmpty2;
                }
                }
            }
        }
    //this->firstEmpty=0;
    //changeFirstEmpty();
    delete [] this->nodes;
    this->nodes=newNodes;
}

std::pair<int,int> Matrix::positionOf(TElem elem) const {
    //Complexity:
    //O(n*m*size)
    std::pair<int, int> pair;
    for (int i=0; i<=nrLines(); i++)
        for (int j=0; j<nrColumns(); j++)
            if (element(i, j)==elem) {
                pair.first=i;
                pair.second=j;
                return pair;
            }
    pair.first=-1;
    pair.second=-1;
    return pair;
}


