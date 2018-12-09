using UnityEngine;
using System.Collections;

public class RemoveFog : MonoBehaviour
{
    void OnTriggerExit (Collider other)
	{
		if(RenderSettings.fog == true){
			RenderSettings.fog = false;
		}
		else{
			RenderSettings.fog = true;
		}
	}
}
