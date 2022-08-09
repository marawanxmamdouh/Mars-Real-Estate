package dev.marawanxmamdouh.marsrealestate.overview

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.marawanxmamdouh.marsrealestate.R
import dev.marawanxmamdouh.marsrealestate.databinding.FragmentOverviewBinding
import dev.marawanxmamdouh.marsrealestate.network.MarsApiFilter

/**
 * This fragment shows the the status of the Mars real-estate web services transaction.
 */
class OverviewFragment : Fragment() {

    /**
     * Lazily initialize our [OverviewViewModel].
     */
    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this)[OverviewViewModel::class.java]
    }

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentOverviewBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        binding.photosGrid.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        })

        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner) {
            if (it != null) {
                this.findNavController().navigate(OverviewFragmentDirections.actionShowDetail(it))
                viewModel.displayPropertyDetailsComplete()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.overflow_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                viewModel.updateFilter(
                    when (menuItem.itemId) {
                        R.id.show_rent_menu -> MarsApiFilter.SHOW_RENT
                        R.id.show_buy_menu -> MarsApiFilter.SHOW_BUY
                        else -> MarsApiFilter.SHOW_ALL
                    }
                )
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}
