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
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import edu.uncc.assignment12.databinding.FragmentSelectDiscountBinding;


public class SelectDiscountFragment extends Fragment {
    ArrayList<String> discounts = new ArrayList<>();
    public SelectDiscountFragment() {
        // Required empty public constructor
    }

    String discount;
    FragmentSelectDiscountBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectDiscountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        discounts.add("10%");
        discounts.add("15%");
        discounts.add("18%");
        discounts.add("Custom");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(layoutManager);
        DiscountAdapter adapter = new DiscountAdapter(discounts, mListener);
        binding.recyclerView.setAdapter(adapter);
        super.onViewCreated(view, savedInstanceState);
        binding.seekBar.setMax(50);
        binding.seekBar.setProgress(25);

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.textViewSeekBarProgress.setText(progress + "%");
                discount = String.valueOf(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelSelectDiscount();
            }
        });
    }

    SelectDiscountListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SelectDiscountListener) {
            mListener = (SelectDiscountListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SelectDiscountListener");
        }
    }

    interface SelectDiscountListener {
        void onDiscountSelected(double discount);

        void onCancelSelectDiscount();
    }
    class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder>{
        public DiscountAdapter(ArrayList<String> data, SelectDiscountListener listener) {
            discounts = data;
            mListener = listener;
        }

        @NonNull
        @Override
        public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.discount_layout, parent, false);  // Create a new layout called item_genre.xml
            return new DiscountViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
            String discount = discounts.get(position);
            holder.bind(discount);
        }

        @Override
        public int getItemCount() {
            return discounts.size();
        }

        class DiscountViewHolder extends RecyclerView.ViewHolder{
            FragmentSelectDiscountBinding mBinding;
            TextView discountAmount;
            public DiscountViewHolder(View itemView) {
                super(itemView);
                discountAmount = itemView.findViewById(R.id.discountAmount);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (!discounts.get(position).equals("Custom")) {
                            discount = discounts.get(position).substring(0,2);
                        }
                        double finalDiscount = Double.valueOf(discount);
                        mListener.onDiscountSelected(finalDiscount);
                    }
                });
            }
            public void bind(String discount) {
                discountAmount.setText(discount);
            }
        }
    }
}