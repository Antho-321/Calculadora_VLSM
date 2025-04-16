package ec.edu.utn.example.calculadora_vlsm;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private LinearLayout hostInputContainer;
    private List<EditText> hostInputFields;
    private Button calculateButton;
    private EditText ipBaseField;
    private Spinner cidrCombo;
    private EditText numSubredesEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        ipBaseField = findViewById(R.id.ip_base_entry);
        cidrCombo = findViewById(R.id.cidr_combo);
        numSubredesEditText = findViewById(R.id.num_subredes_spin);
        hostInputContainer = findViewById(R.id.host_frame);
        calculateButton = findViewById(R.id.calc_button);

        // Initialize the list for input fields
        hostInputFields = new ArrayList<>(); // Initialize the list to avoid NullPointerException

        // Set up CIDR Spinner
        ArrayAdapter<CharSequence> cidrAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.cidr_values, // CIDR values should be defined in strings.xml
                android.R.layout.simple_spinner_item
        );
        cidrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cidrCombo.setAdapter(cidrAdapter);

        // Add text change listener for subnets input field
        numSubredesEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                updateHostFields(); // Update host fields when the number of subnets changes
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        // Set onClickListener for the calculate button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCalculateClicked(v);
            }
        });
    }

    // This method updates the host input fields based on the number of subnets
    private void updateHostFields() {
        hostInputContainer.removeAllViews(); // Clear the existing input fields

        int numSubnets = 0;
        try {
            numSubnets = Integer.parseInt(numSubredesEditText.getText().toString());
        } catch (NumberFormatException e) {
            // Handle invalid number of subnets input
            return;
        }

        hostInputFields.clear();  // Clear the host input fields list to avoid memory issues

        for (int i = 0; i < numSubnets; i++) {
            // Create a new host input field for each subnet
            TextView label = new TextView(this);
            label.setText("Hosts para Subred " + (i + 1) + ":");
            hostInputContainer.addView(label);

            EditText hostField = new EditText(this);
            hostField.setHint("Ingresa número de hosts");
            hostInputFields.add(hostField);
            hostInputContainer.addView(hostField);
        }
    }

    // This method is called when the "Calcular Subredes" button is clicked
    public void onCalculateClicked(View view) {
        // Get the host values from the input fields
        List<Integer> hosts = new ArrayList<>();
        for (EditText hostField : hostInputFields) {
            try {
                hosts.add(Integer.parseInt(hostField.getText().toString()));
            } catch (NumberFormatException e) {
                // Handle invalid host input
                hostField.setError("Por favor, ingresa un número válido.");
                return;
            }
        }

        // Get IP and CIDR values
        String ipBase = ipBaseField.getText().toString();
        String cidr = (String) cidrCombo.getSelectedItem();

        // Perform the VLSM calculation (use a separate class for calculation, if needed)
        VLSMCalculator calculator = new VLSMCalculator(ipBase, hosts, Integer.parseInt(cidr));
        try {
            List<VLSMCalculator.SubnetResult> results = calculator.calculate();
            showResultsDialog(results);  // Show results in a dialog with table
        } catch (Exception e) {
            showPopupMessage("Error: " + e.getMessage());
        }
    }

    // This method shows the calculated subnet results in a dialog with a table
    // Esta es la versión actualizada del método showResultsDialog
    private void showResultsDialog(List<VLSMCalculator.SubnetResult> results) {
        // Crear el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Resultados de Subredes VLSM");

        // Inflar el diseño personalizado para el diálogo
        View dialogView = LayoutInflater.from(this).inflate(R.layout.subnet_results_layout, null);
        builder.setView(dialogView);

        // Obtener el TableLayout desde el diseño
        TableLayout tableLayout = dialogView.findViewById(R.id.results_table);

        // Llenar la tabla con los resultados
        boolean alternateRowColor = false;  // Usar colores alternos para las filas
        for (VLSMCalculator.SubnetResult subnet : results) {
            TableRow row = new TableRow(this);

            // Establecer colores alternos para las filas
            if (alternateRowColor) {
                row.setBackgroundColor(Color.parseColor("#F2F2F2"));
            } else {
                row.setBackgroundColor(Color.WHITE);
            }
            alternateRowColor = !alternateRowColor;

            // Agregar celdas con los datos de la subred
            addCell(row, "Subred " + subnet.getSubredId());
            addCell(row, subnet.getIpBase());
            addCell(row, "/" + subnet.getCidr());
            addCell(row, String.valueOf(subnet.getHostsRequested()));
            addCell(row, String.valueOf(subnet.getHostsAvailable()));
            addCell(row, subnet.getRangeIps());
            addCell(row, subnet.getBroadcast());

            // Agregar la fila a la tabla
            tableLayout.addView(row);
        }

        // Agregar botones para cerrar el diálogo
        builder.setPositiveButton("Cerrar", null);

        // Crear y mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Método auxiliar para agregar una celda a una fila de la tabla
    private void addCell(TableRow row, String text) {
        TextView cell = new TextView(this);
        cell.setText(text);
        cell.setPadding(16, 12, 16, 12);  // Aplicar el padding a cada celda
        cell.setTextColor(ContextCompat.getColor(this, R.color.text_primary));  // Usar el color primario del texto
        cell.setBackgroundResource(R.drawable.table_cell_bg);  // Usar el fondo de la celda
        row.addView(cell);
    }

    // Utility method to show popup messages
    private void showPopupMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}