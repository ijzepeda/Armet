package com.ijzepeda.armet.activity;

public class BasicKnowledge {

    /**

     armet:{

     usuario:[],
     cliente:[],
     servicio:[],
     producto:[],
     tareas:[
     usuario_tecnico_id,
     id,
     ubicacion,
     locacion,
     fecha/hora_inicio
     fecha/hora final
     cliente id=?servicio_id
     ],
     dia:[
     fecha, locacion...
     usuario_tecnico_id,
     tareas[tareas id],
     ]


     ]//relaciones?


Day basically stores ids: for User, Tasks , Services. and normal Daate

     At this point Day saves a list of ids for tasks and services.... it might work for easy handling BUT
     when loading the list for the firsTime [or creating the Excel]
     I will need to do a fetch for each id
     foreach{
     dataSnapshot.child(id)
     ref.child(id).getListenerOnce?{snapshot.convertToClassObject}

     I am also missign an adapter for tasks
     }

     * */


}
