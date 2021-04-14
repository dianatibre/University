#include "SortedBag.h"
#include "SortedBagIterator.h"

SortedBag::SortedBag(Relation r) {
	//TODO - Implementation
    //O(1)
    this->root = nullptr;
    this->length = 0;
    this->relation = r;
}

BSTNode* SortedBag::initNode(TComp e, BSTNode* parent) {
    //O(1)
    BSTNode* node = new BSTNode;
    node->info=e;
    node->left=nullptr;
    node->right=nullptr;
    node->parent=parent;
    return node;
}

BSTNode* SortedBag::insert_rec(BSTNode* node, TComp e) {
    if (node==nullptr) {
        node=this->initNode(e,nullptr);
    }
    else if (node->info<=e) {
        node->left=insert_rec(node->left, e);
    }
    else {
        node->right=insert_rec(node->right, e);
    }
    return node;
}

void SortedBag::add(TComp e) {
	//TODO - Implementation
    //Complexity:
    //BC: Theta(log2n)
    //WC: Theta(log2n)
    //AC: 0(log2n)
    
    /*
     this->insert_rec(this->root, e);
     this->length++;
      */
    
    
    if(this->root==nullptr)
        this->root = this->initNode(e, nullptr);
    else
    {
        BSTNode* parent = nullptr;
        BSTNode* currentNode = this->root;
        while (currentNode != nullptr)
        {
            if (this->relation(e, currentNode->info))
            {
                parent = currentNode;
                currentNode = currentNode->left;
            }
            else
            {
                parent = currentNode;
                currentNode = currentNode->right;
            }
        }
        currentNode = this->initNode(e, parent);
        if (this->relation(e, parent->info))
            parent->left = currentNode;
        else
            parent->right = currentNode;
    }
    this->length++;
}


BSTNode* SortedBag::minimum(BSTNode* node) {
    //Complexity: O(log2n)
    //BC: Theta(1)
    //WC: Theta(n)
    //AC: O(log2n)
    BSTNode* currentNode = node;
    if (currentNode == nullptr)
        return nullptr;
    else {
        while (currentNode->left != nullptr) {
            currentNode = currentNode->left;
        }
        return currentNode;
    }
}


BSTNode* SortedBag::maximum(BSTNode* node) {
    //Complexity: O(log2n)
    //BC: Theta(1)
    //WC: Theta(n)
    //AC: O(log2n)
    BSTNode* currentNode = node;
    if (currentNode == nullptr)
        return nullptr;
    else {
        while (currentNode->right != nullptr) {
            currentNode = currentNode->right;
        }
        return currentNode;
    }
}


bool SortedBag::remove(TComp e) {
	//TODO - Implementation
    //Complexity:
    //BC: Theta(log2n)
    //WC: Theta(n)
    //AC: 0(log2n)
	BSTNode* currentNode = this->root;
    if (currentNode == nullptr)
    {
        return false;
    }
    while (currentNode != nullptr && currentNode->info != e)
    {
        if (this->relation(e, currentNode->info))
            currentNode = currentNode->left;
        else
            currentNode = currentNode->right;
    }
    if (currentNode == nullptr)
        return false;
    this->length--;
    //if the node has 2 children
    if (currentNode->left != nullptr && currentNode->right != nullptr)
    {
        BSTNode* max = this->maximum(currentNode->left);
        currentNode->info = max->info;
        if (max->parent == currentNode) {
            currentNode->left = max->left;
            if (max->left != nullptr)
                max->left->parent = currentNode;
            delete max;
        }
        else {
            max->parent->right = max->left;
            if (max->left != nullptr)
                max->left->parent = max->parent;
            delete max;
        }
        return true;
    }
    //if the node has 1 child
    else if (currentNode->left != nullptr || currentNode->right != nullptr) {
        //just left child
        if (currentNode->left != nullptr)
        {
            //if current node is the root
            if (currentNode->parent == nullptr) {
                this->root = currentNode->left;
                this->root->parent = nullptr;
                delete currentNode;
            }
            //if the current node is not the root
            else {
                if (currentNode->parent->left == currentNode)
                    currentNode->parent->left = currentNode->left;
                else if (currentNode->parent->right == currentNode)
                    currentNode->parent->right = currentNode->left;
                if (currentNode->left != nullptr)
                    currentNode->left->parent = currentNode->parent;
                delete currentNode;
            }
        }
        //just right child
        else
        {
            //if current node is the root
            if (currentNode->parent == nullptr) {
                this->root = currentNode->right;
                this->root->parent = nullptr;
                delete currentNode;
            }
            else {
                if(currentNode->parent->left == currentNode)
                    currentNode->parent->left = currentNode->right;
                else if(currentNode->parent->right == currentNode)
                    currentNode->parent->right = currentNode->right;
                if (currentNode->right != nullptr)
                    currentNode->right->parent = currentNode->parent;
                delete currentNode;
            }
        }
    }
    //if there are no children
    else {
        if (currentNode->parent == nullptr) {
            this->root = nullptr;
            delete currentNode;
        }
        else {
            if (currentNode->parent->left == currentNode) {
                currentNode->parent->left = nullptr;
                delete currentNode;
            }
            else {
                currentNode->parent->right = nullptr;
                delete currentNode;
            }
        }
    }
    return true;
}

bool SortedBag::search_rec(BSTNode* node, TComp elem) const {
    //Complexity: O(n)
    if (node == nullptr)
        return false;
    else {
        if (node->info==elem) {
            return true;
        }
        else if (this->relation(elem, node->info))
            return this->search_rec(node->left, elem);
        else
            return this->search_rec(node->right, elem);
    }
}

bool SortedBag::search(TComp elem) const {
	//TODO - Implementation
    //Complexity: O(n)
    //BC: O(1)
    //WC: O(n)
    //AC: O(log2n)
	return this->search_rec(this->root, elem);
}

int SortedBag::nrOccurences_rec(BSTNode* node, TComp e) const {
    if (node == nullptr)
        return 0;
    else
        if (node->info == e)
            return this->nrOccurences_rec(node->left, e) + 1;
        else
            return this->nrOccurences_rec(node->left, e) + this->nrOccurences_rec(node->right, e);
}

int SortedBag::nrOccurrences(TComp elem) const {
	//TODO - Implementation
    //Complexity: O(n)
	BSTNode* currentNode = this->root;
    if (currentNode == nullptr)
        return 0;
    else {
        if (currentNode->info == elem)
            return nrOccurences_rec(currentNode, elem);
        else
        {
            while (currentNode != nullptr && currentNode->info != elem) {
                if (this->relation(elem, currentNode->info))
                    currentNode = currentNode->left;
                else
                    currentNode = currentNode->right;
            }
            if (currentNode == nullptr)
                return 0;
            else if (currentNode->info == elem)
                return nrOccurences_rec(currentNode, elem);
        }
    }
    return 0;
}



int SortedBag::size() const {
	//TODO - Implementation
    //Complexity: O(1)
	return this->length;
}


bool SortedBag::isEmpty() const {
	//TODO - Implementation
    //Complexity: O(1)
    if (this->length==0)
        return true;
	return false;
}


SortedBagIterator SortedBag::iterator() const {
	return SortedBagIterator(*this);
}

bool searchElemInVector(std::vector<TElem> vector, TElem e) {
    //Complexity:
    //BC: O(1)
    //WC: O(n)
    //AC: O(n)
    for (int i=0; i<vector.size(); i++)
        if (vector[i]==e)
            return true;
    return false;
}

int SortedBag::uniqueCount() const {
    //Complexity: O(n^3)
    int count = 0;
    std::vector<TElem> alreadyfound;
    if (this->isEmpty())
        return 0;
    else {
        SortedBagIterator it = this->iterator();
        it.first();
        while (it.valid()) {
            TElem e=it.getCurrent();
            it.next();
            if (this->nrOccurrences(e)==1 && searchElemInVector(alreadyfound, e)==false) {
                count++;
                alreadyfound.push_back(e);
            }
        }
    }
    return count;
}


SortedBag::~SortedBag() {
	//TODO - Implementation
    //Complexity: O(n)
    std::stack<BSTNode*> stack;
    if (this->root != nullptr)
        stack.push(this->root);
    while (!stack.empty())
    {
        BSTNode* currentNode = stack.top();
        stack.pop();
        if (currentNode->right != nullptr)
            stack.push(currentNode->right);
        if (currentNode->left != nullptr)
            stack.push(currentNode->left);
        delete currentNode;
    }
}
