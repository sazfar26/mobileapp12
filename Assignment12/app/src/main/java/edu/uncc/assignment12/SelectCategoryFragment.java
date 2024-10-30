package edu.uncc.assignment12;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.uncc.assignment12.databinding.FragmentSelectCategoryBinding;
import edu.uncc.assignment12.databinding.FragmentSelectDiscountBinding;


public class SelectCategoryFragment extends Fragment {

    String[] mCategories = {"Housing", "Transportation", "Food", "Health", "Other"};

    public SelectCategoryFragment() {
        // Required empty public constructor
    }

    FragmentSelectCategoryBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(layoutManager);
        SelectCategoryFragment.CategoryAdapter adapter = new SelectCategoryFragment.CategoryAdapter(mCategories, mListener);
        binding.recyclerView.setAdapter(adapter);
        super.onViewCreated(view, savedInstanceState);
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelSelectCategory();
            }
        });


    }

    SelectCategoryListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SelectCategoryListener) {
            mListener = (SelectCategoryListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SelectCategoryListener");
        }
    }

    interface SelectCategoryListener {
        void selectCategory(String category);
        void onCancelSelectCategory();
    }
    class CategoryAdapter extends RecyclerView.Adapter<SelectCategoryFragment.CategoryAdapter.CategoryViewHolder>{
        public CategoryAdapter(String[] data, SelectCategoryFragment.SelectCategoryListener listener) {
            mCategories = data;
            mListener = listener;
        }

        @NonNull
        @Override
        public SelectCategoryFragment.CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.category_layout, parent, false);
            return new SelectCategoryFragment.CategoryAdapter.CategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
            String category = mCategories[position];
            holder.bind(category);
        }

        @Override
        public int getItemCount() {
            return mCategories.length;
        }

        class CategoryViewHolder extends RecyclerView.ViewHolder{
            FragmentSelectCategoryBinding mBinding;
            TextView categoryName;
            public CategoryViewHolder(View itemView) {
                super(itemView);
                categoryName = itemView.findViewById(R.id.categoryName);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        String category = mCategories[position];
                        mListener.selectCategory(category);
                    }
                });
            }
            public void bind(String category) {
                categoryName.setText(category);
            }
        }
    }

}