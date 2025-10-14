package com.example.assignment3opsc.ui.groups


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment3opsc.data.group.GroupEntity
import com.example.assignment3opsc.databinding.FragmentGroupsBinding


class GroupsFragment : Fragment() {

    private var _binding: FragmentGroupsBinding? = null
    private val binding get() = _binding!!

    private val vm: GroupsViewModel by viewModels {
        GroupsVMFactory(requireActivity().application)
    }
    private lateinit var groupAdapter: GroupAdapter
    private val trendingAdapter = TrendingAdapter()
    private var selectedForExit: GroupEntity? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        // groups list (horizontal)
        groupAdapter = GroupAdapter(
            onClick = { group ->
                selectedForExit = group
                Toast.makeText(requireContext(), "Selected: ${group.name}", Toast.LENGTH_SHORT).show()
                // TODO: Navigate to group detail if you have it
            },
            onLongPress = { group ->
                confirmExit(group)
            }
        )
        binding.rvGroups.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = groupAdapter
        }

        // trending grid (2 columns)
        binding.rvTrending.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = trendingAdapter
        }
        trendingAdapter.submit(
            listOf(
                TrendingItem("https://image.tmdb.org/t/p/w500/5GynP6w2OQWSbKnCLHrBIriF4CC.jpg"), // The Conjuring 2
                TrendingItem("https://image.tmdb.org/t/p/w500/krX87nGZ9E9QnUnxE27XGr0CcsM.jpg")  // Crazy Rich Asians
            )
        )

        // observe groups
        vm.groups.observe(viewLifecycleOwner, Observer { list ->
            groupAdapter.submit(list)
            // show starter sample groups if first run
            if (list.isEmpty()) {
                vm.createGroup("Weekend Watchers", "🍿")
                vm.createGroup("Horror Squad", "👻")
                vm.createGroup("Comedy Club", "😂")
            }
        })

        // actions row
        binding.btnCreate.setOnClickListener { showCreateDialog() }
        binding.btnJoin.setOnClickListener { showJoinDialog() }
        binding.btnExit.setOnClickListener {
            selectedForExit?.let { confirmExit(it) } ?: Toast.makeText(requireContext(),
                "Long-press or tap a group first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showCreateDialog() {
        val input = EditText(requireContext())
        input.hint = "Group name (e.g., Weekend Watchers)"
        AlertDialog.Builder(requireContext())
            .setTitle("Create group")
            .setView(input)
            .setPositiveButton("Create") { _, _ ->
                val name = input.text?.toString().orEmpty()
                if (name.isBlank()) {
                    Toast.makeText(requireContext(),"Please enter a name", Toast.LENGTH_SHORT).show()
                } else vm.createGroup(name)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // mock “join by code” -> we just create a group using the code/name
    private fun showJoinDialog() {
        val input = EditText(requireContext())
        input.hint = "Enter invite code or group name"
        AlertDialog.Builder(requireContext())
            .setTitle("Join group")
            .setView(input)
            .setPositiveButton("Join") { _, _ ->
                val text = input.text?.toString().orEmpty()
                if (text.isBlank()) {
                    Toast.makeText(requireContext(),"Enter a code/name", Toast.LENGTH_SHORT).show()
                } else vm.createGroup(text, "🙌")
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun confirmExit(group: GroupEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit ${group.name}?")
            .setMessage("You will be removed from this group.")
            .setPositiveButton("Exit") { _, _ -> vm.exitGroup(group.id) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
