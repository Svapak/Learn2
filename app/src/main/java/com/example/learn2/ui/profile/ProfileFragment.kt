package com.example.learn2.ui.profile


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions
import com.example.learn2.MainActivity
import com.example.learn2.R
import com.example.learn2.StudentSIgnin
import com.example.learn2.databinding.FragmentProfileBinding
import com.example.learn2.editProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dataModel.InfoModel


class ProfileFragment : Fragment()  {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileBinding.inflate(layoutInflater)
//        val uid: String=FirebaseAuth.getInstance().currentUser!!.uid
//        if(FirebaseAuth.getInstance().currentUser!!.uid!=null){
//            Toast.makeText(context,FirebaseAuth.getInstance().currentUser!!.uid,Toast.LENGTH_SHORT).show()
//        }

//        FirebaseDatabase.getInstance().getReference("Students").child(uid).get()
//            .addOnSuccessListener {
//                if(it.exists()){
//                    val data= it.getValue(InfoModel::class.java)
//                    binding.tname.setText(data!!.FirstName.toString())
//                    binding.ttname.setText(data.LastName.toString())
//                    binding.tboard.setText(data.Board.toString())
//                    binding.tclass.setText(data.Class.toString())
//                    binding.tinstitute.setText(data.Institute.toString())
//                    binding.tstate.setText(data.State.toString())
//                }
//                else{
//                    Toast.makeText(context,"Saala ho kya rha h",Toast.LENGTH_SHORT).show()
//                }
//            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        userId?.let{uid->
            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("Students").child(uid)

            reference.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val infoModel = snapshot.getValue(InfoModel::class.java)
                        infoModel?.let { data->
                            binding.tname.setText(data.firstName)
                            binding.ttname.setText(data.lastName)
                            binding.tclass.setText(data.uclass)
                            binding.tboard.setText(data.board)
                            binding.tinstitute.setText(data.institute)
                            binding.tstate.setText(data.state)
                            Glide.with(this@ProfileFragment).load(data.image).into(binding.timage)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context,"ooo",Toast.LENGTH_SHORT)
                }
            })
        }
        binding.editprofile.setOnClickListener{
            val intent=Intent(this@ProfileFragment.context,editProfile::class.java)
            startActivity(intent)
        }
        binding.logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent= Intent(this@ProfileFragment.context,MainActivity::class.java)
            startActivity(intent)
        }

    }
}