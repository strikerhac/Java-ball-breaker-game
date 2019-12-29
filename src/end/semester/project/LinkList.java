package end.semester.project;


//                        Rao Nouman Ahmad And Hassaan Akbar Cheema
//                        131937 & 174351
//                        BSCS-6C



class LinkList
{
// Simple link list is buit here
    // example is consdeir t q r
    // t only know or hass refrence of next q
    // now consider q and r and q has only refrence of q 
    // so we are dealing only two refrences at time means next state and previous state
        int J,K;
	LinkList First_Link;
	LinkList Link_List;
	public LinkList(int J,int K)
	{
		this.J=J;
		this.K=K;
		First_Link=null;
		Link_List=null;
	}
	public LinkList(LinkList p)
	{
		J=p.J;
		K=p.K;
		First_Link=null;
		Link_List=null;
	
	}
	public void setNext(LinkList lnode)
	{
		First_Link=lnode;
	}
	public LinkList getNext()
	{
		return First_Link;
	}
	public void setPrev(LinkList lnode)
	{
		Link_List=lnode;
	}
	public LinkList getPrev()
	{
		return Link_List;
	}
	public int Return_X_Coordinate()
	{
		return J;
	}
	public int Return_Y_Coordinate()
	{
		return K;
	}
}