package com.victormagosso.vfood.activity.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.victormagosso.vfood.R
import com.victormagosso.vfood.adapter.company.AdapterLastAddedDishesMock
import com.victormagosso.vfood.adapter.company.AdapterMostSelling
import com.victormagosso.vfood.mockdata.mockmodels.LastAddedDishesMock
import com.victormagosso.vfood.mockdata.mockmodels.MostSellingMock


class HomeFragment : Fragment() {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_home, container, false)
        var recyclerLastAddedDishesMock = view.findViewById<RecyclerView>(R.id.recyclerLastAddedDishes)
        var recyclerMostSelling = view.findViewById<RecyclerView>(R.id.recyclerMostSold)

        recyclerLastAddedDishesMock?.adapter = AdapterLastAddedDishesMock(createMockLastAddedDishes())
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerLastAddedDishesMock?.layoutManager = gridLayoutManager;
        recyclerLastAddedDishesMock?.hasFixedSize()

        recyclerMostSelling?.adapter = AdapterMostSelling(createMockMostSelling())
        val gridLayoutManagerMostSelling = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerMostSelling?.layoutManager = gridLayoutManagerMostSelling;
        recyclerMostSelling?.hasFixedSize()

//        var sair = view?.findViewById<Button>(com.victormagosso.vfood.R.id.logoff)
//        sair?.setOnClickListener {
//            auth.signOut()
//            activity?.finish()
//        }
//        auth.signOut()
//        activity?.finish()
        return view
    }

    private fun createMockLastAddedDishes(): List<LastAddedDishesMock> {
        return listOf(
            LastAddedDishesMock("Yakisoba", R.drawable.foodone, "Adicionado em 11/12/2020"),
            LastAddedDishesMock("Strogonoff", R.drawable.foodfive, "Adicionado em 10/12/2020"),
            LastAddedDishesMock("Risotto", R.drawable.foodthree, "Adicionado em 03/12/2020"),
            LastAddedDishesMock("Macarrão com queijo", R.drawable.foodtwo, "Adicionado em 03/12/2020"),
            LastAddedDishesMock("Arroz grego", R.drawable.foodfour, "Adicionado em 24/11/2020"),
            LastAddedDishesMock("Frango Xadrez", R.drawable.foodsix, "Adicionado em 20/11/2020")
        )
    }

    private fun createMockMostSelling(): List<MostSellingMock> {
        return listOf(
            MostSellingMock("12", "Churrasco", R.drawable.mostone, "11/12/2020"),
            MostSellingMock("11", "Espetinho", R.drawable.mosttwo, "11/12/2020"),
            MostSellingMock("9", "Feijoada", R.drawable.mostthree, "09/12/2020"),
            MostSellingMock("7", "Baião de Dois", R.drawable.mostfour, "12/12/2020"),
            MostSellingMock("6", "Linguiça Calabresa", R.drawable.mostfive, "02/12/2020"),
            MostSellingMock("6", "Strogonoff", R.drawable.foodfive, "10/12/2020")
        )
    }
}