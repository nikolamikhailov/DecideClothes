package ru.l4gunner4l.decideclothes.ui.admin.sales.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.l4gunner4l.decideclothes.base.SingleLiveEvent
import ru.l4gunner4l.decideclothes.data.MainRepository
import ru.l4gunner4l.decideclothes.local.entity.*
import ru.terrakok.cicerone.Router

class DetailsSaleViewModel(
    private val firstSale: Sale?,
    private val repo: MainRepository,
    private val router: Router
) : ViewModel() {

    private val kindsLiveData = MutableLiveData<List<KindEntity>>(
        //emptyList()
    )
    val kinds: LiveData<List<KindEntity>>
        get() = kindsLiveData

    private val saleLiveData = MutableLiveData<DetailsSale>(
        //firstSale?.toDetailsSale(false)
    )
    val sale: LiveData<DetailsSale>
        get() = saleLiveData

    private val toastSingleLiveEvent = SingleLiveEvent<String>()
    val errorToast: LiveData<String>
        get() = toastSingleLiveEvent



    fun requestInfo() {
        viewModelScope.launch {
            val kinds = repo.readAllKinds()
            kindsLiveData.postValue(kinds)
        }
    }

    fun kindSelected(
        position: Int
    ) {
        viewModelScope.launch {
            val kind = kindsLiveData.value!![position]
            val goods = repo.searchByKindId(kind.id)

            if (firstSale == null) {
                saleLiveData.postValue(
                    DetailsSale(
                        name = "",
                        kind = kind,
                        goods = goods.map {
                            it.toCheckedGood(false)
                        }
                    )
                )
            } else {
                val sg = repo.readGoodsIdsBySaleId(firstSale.id)

                saleLiveData.postValue(
                    DetailsSale(
                        id = firstSale.id,
                        name = firstSale.name,
                        kind = kind,
                        goods = goods.map { goodEntity ->
                            goodEntity.toCheckedGood(
                                sg.contains(goodEntity.id)
                            )
                        }
                    )
                )
            }
        }
    }

    fun onGoodCheckClicked(position: Int, isChecked: Boolean) {
        viewModelScope.launch {
            val newList = saleLiveData.value!!.goods
                .apply {
                    get(position).isChecked = isChecked
                }
            saleLiveData.postValue(
                saleLiveData.value!!.copy(
                    goods = newList
                )
            )
        }
    }


    fun onSaveClick(name: String) {
        viewModelScope.launch {

            SaleEntity(
                id = saleLiveData.value!!.id,
                name = name,
                kindId = saleLiveData.value!!.kind.id
            ).let { sale ->
                if (firstSale != null) repo.updateSale(sale)
                else repo.addSale(sale)

                repo.deleteSaleGoodsBySaleId(sale.id)
                saleLiveData.value!!.goods
                    .filter { it.isChecked }
                    .map { it.id }
                    .forEach { goodId ->
                        repo.addSaleGood(
                            SaleGoodEntity(
                                saleId = sale.id,
                                goodId = goodId,
                            )
                        )
                    }
            }
            onExitClick()
        }
    }

    fun onExitClick() = router.exit()
}
