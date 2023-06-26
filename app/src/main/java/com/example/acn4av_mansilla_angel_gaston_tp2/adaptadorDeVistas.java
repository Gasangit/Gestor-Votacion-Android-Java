package com.example.acn4av_mansilla_angel_gaston_tp2;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class adaptadorDeVistas extends RecyclerView.Adapter<adaptadorDeVistas.ViewHolder>{

    private VotacionObjeto[] localDataSet;
    private UsuarioObjeto[] localDatasetUsers;
    private String idUser;

    private final String  presentClass = this.getClass().getSimpleName();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final String  presentClass = this.getClass().getSimpleName().toString();

        public ViewHolder(View view) {
            super(view);
            Log.d(presentClass, ">>>Ingreso a contructor VIEWHOLDER ...");
            //aca se puede llamar al click listener
            textView = view.findViewById(R.id.itemStructure);
        }
        public TextView getTextView() {
            return textView;
        }
    }

    public adaptadorDeVistas(VotacionObjeto[] dataSet) {
        Log.d(presentClass, ">>>Se ejecuta constructor de ADAPTADOR con objeto VotacionObjeto");
        localDataSet = dataSet;
    }

    public adaptadorDeVistas(VotacionObjeto[] dataSet, String idUser) {
        Log.d(presentClass, ">>>Se ejecuta constructor de ADAPTADOR con objeto VotacionObjeto y idUser");
        localDataSet = dataSet;
        this.idUser = idUser;
    }

    public adaptadorDeVistas(UsuarioObjeto [] dataSet, String idUser) {
        Log.d(presentClass, ">>>Se ejecuta constructor de ADAPTADOR con objeto UsuarioObjeto y idUser");
        localDatasetUsers = dataSet;
        this.idUser = idUser;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lista_votaciones, viewGroup, false);
        Log.d(presentClass, ">>>Creando nuevo VIEWHOLDER ....");
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        String contextOfView = viewHolder.getTextView().getContext().getClass().getSimpleName().toString();

        if(contextOfView.equalsIgnoreCase("CrearVotacion") || contextOfView.equalsIgnoreCase("MisVotaciones"))
        {
            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            Log.d(presentClass, ">>>Agregando texto de DATASET a TEXTVIEW de VIEWHOLDER posicion "+ position + " ...");
            viewHolder.getTextView().setText(localDataSet[viewHolder.getAdapterPosition()].getDescripcion());
            viewHolder.getTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle votingData = new Bundle();
                    votingData.putString("idVotacion", localDataSet[viewHolder.getAdapterPosition()].getIdVotacion());
                    votingData.putString("idOrganizador", localDataSet[viewHolder.getAdapterPosition()].getIdOrganizador());
                    votingData.putString("descripcion", localDataSet[viewHolder.getAdapterPosition()].getDescripcion());
                    votingData.putString("idUser", idUser);

                    if(contextOfView.equalsIgnoreCase("MisVotaciones"))
                    {
                        String opcion1 = localDataSet[viewHolder.getAdapterPosition()].getOpcion1();
                        String opcion1Id = localDataSet[viewHolder.getAdapterPosition()].getOpcion1Id();
                        String opcion2 = localDataSet[viewHolder.getAdapterPosition()].getOpcion2();
                        String opcion2Id = localDataSet[viewHolder.getAdapterPosition()].getOpcion2Id();
                        String opcion3 = localDataSet[viewHolder.getAdapterPosition()].getOpcion3();
                        String opcion3Id = localDataSet[viewHolder.getAdapterPosition()].getOpcion3Id();

                        votingData.putString("opcion1", opcion1);
                        votingData.putString("opcion1Id", opcion1Id);
                        votingData.putString("opcion2", opcion2);
                        votingData.putString("opcion2Id", opcion2Id);
                        votingData.putString("opcion3", opcion3);
                        votingData.putString("opcion3Id", opcion3Id);

                        Log.d(presentClass, "\n OPCION 1 : ID : " + opcion1Id + " DESCRIPCION : " + opcion1 +
                                "\n OPCION 2 : ID : " + opcion2Id + " DESCRIPCION : " + opcion2 +
                                "\n OPCION 3 : ID : " + opcion3Id + " DESCRIPCION : " + opcion3);

                        Intent gotoMyVote = new Intent(viewHolder.getTextView().getContext(), MiVoto.class );
                        gotoMyVote.putExtras(votingData);
                        viewHolder.getTextView().getContext().startActivity(gotoMyVote);
                    }else {
                        Intent gotoOneVoting = new Intent(viewHolder.getTextView().getContext(), VerItemVotacion.class );
                        gotoOneVoting.putExtras(votingData);
                        viewHolder.getTextView().getContext().startActivity(gotoOneVoting);
                    }
                }
            });
        }

        if(contextOfView.equalsIgnoreCase("UsersManager")) {

            String itemText = localDatasetUsers[viewHolder.getAdapterPosition()].getNombre() +
                    " " + localDatasetUsers[viewHolder.getAdapterPosition()].getApellido();
            Log.d(presentClass, "Usuario en item : " + itemText);
            viewHolder.getTextView().setText(itemText);

            viewHolder.getTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle userData = new Bundle();
                    userData.putString("idUser", idUser);
                    userData.putString("idItemUser", localDatasetUsers[viewHolder.getAdapterPosition()].getId());
                    userData.putString("nombre", localDatasetUsers[viewHolder.getAdapterPosition()].getNombre());
                    userData.putString("apellido", localDatasetUsers[viewHolder.getAdapterPosition()].getApellido());
                    userData.putString("dni", localDatasetUsers[viewHolder.getAdapterPosition()].getDni());
                    userData.putString("mail", localDatasetUsers[viewHolder.getAdapterPosition()].getMail());
                    userData.putString("password", localDatasetUsers[viewHolder.getAdapterPosition()].getPass());
                    userData.putString("calle", localDatasetUsers[viewHolder.getAdapterPosition()].getCalle());
                    userData.putString("altura", localDatasetUsers[viewHolder.getAdapterPosition()].getAltura());
                    userData.putBoolean("admin",localDatasetUsers[viewHolder.getAdapterPosition()].getAdmin());

                    Intent goToItemUser = new Intent(viewHolder.getTextView().getContext(), VerItemUsuario.class );
                    goToItemUser.putExtras(userData);
                    viewHolder.getTextView().getContext().startActivity(goToItemUser);
                }
            });

        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(localDataSet != null) {
            return localDataSet.length;
        }
        else{
            return localDatasetUsers.length;
        }
    }

}
