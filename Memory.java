import javax.naming.InsufficientResourcesException;

/*This class describes the short term memory of the virtual machine
* This functions by corresponding 4 byte words represented as 8 hex characters.
* A memory declaration initializes 8 cores of memory containing 128 4 byte word partitions.
* The main functions of this are the fetch and store functions.*/
public class Memory {
    //This is a temporary store for any set of instructions.
    String[] instructionSet;
    String[] memCore = new String[1024];
    String[] clearCore = new String[1024];

    Memory(){

    }

//Here the entire memory core is fetched.
    public String[] fetchCore(){
        return memCore;
    }
    //This method guarantees that the memCore has space for the data being stored
    //Then the data is copied into the Ram slots as deemed appropriate
    public void store(String[] newData) throws InsufficientResourcesException {
        int j = 0;//This denotes the index of the newData String array
        int i = 0;//This denotes the core slot index
            //Method begins iteration across the memoryCore until it finds an empty slot
            for (i = 0; i < memCore.length; i++) {
                if (memCore[i].isEmpty()&&i+newData.length<=memCore.length) {
                    if (i + newData.length <= memCore.length) {
                        break;
                    }
                }
                //This checks if the newData will iterate outside the bounds of the memCore Storage
                if (i + newData.length > memCore.length) {
                    throw new InsufficientResourcesException();
                }
            }
            //Here the data, after being assured that the data will fit within the RAM,
            //is copied into its respective ram slots
            for (i=i;i<=i+newData.length;i++){
                memCore[i]=newData[j];
                j++;
            }


    }
    //The Fetch method copies each index of the memCore to a new String array to be returned
    //With each iteration, the index in the memCore is cleared.
    public String[] fetch(int length, int index){
        String[] newFetch=new String[length];
        String[] clearFetch=new String[length];
        for (int i=0;i<length;i++){
            newFetch[i]=memCore[i+index];
            memCore[i+index]=clearFetch[i];
        }
        return newFetch;
    }
}
