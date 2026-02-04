    package org.example.pspbackend.controller;

    import org.example.pspbackend.domain.Merchant;
    import org.example.pspbackend.domain.PaymentMethod;
    import org.example.pspbackend.dto.MerchantDTO;
    import org.example.pspbackend.dto.PaymentMethodDTO;
    import org.example.pspbackend.service.PaymentMethodService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping(value = "payment-method", produces = MediaType.APPLICATION_JSON_VALUE)
    public class PaymentMethodController {
        @Autowired
        private PaymentMethodService paymentMethodService;

        @GetMapping("/all")
        public ResponseEntity<List<PaymentMethodDTO>> findAll() {
            List<PaymentMethodDTO> paymentMethodDTOs = paymentMethodService.getAll();
            if(paymentMethodDTOs == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(paymentMethodDTOs);
        }

        @GetMapping("/all-available")
        public ResponseEntity<List<PaymentMethodDTO>> findAllAvailable() {
            List<PaymentMethodDTO> paymentMethodDTOs = paymentMethodService.getAllAvailable();
            if(paymentMethodDTOs == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(paymentMethodDTOs);
        }

        @GetMapping("/merchant/{id}")
        public ResponseEntity<List<PaymentMethodDTO>> findByMerchantId(@PathVariable Long id) {
            List<PaymentMethodDTO> paymentMethodDTOs = paymentMethodService.getByMerchantId(id);
            if(paymentMethodDTOs == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(paymentMethodDTOs);
        }

        @PostMapping("/create")
        public ResponseEntity<PaymentMethodDTO> create(@RequestBody PaymentMethodDTO paymentMethodDTO) {
            PaymentMethodDTO savedDTO = paymentMethodService.save(paymentMethodDTO);
            if (savedDTO == null) {
                return ResponseEntity.internalServerError().build();
            }
            return ResponseEntity.ok(savedDTO);
        }

        @PutMapping("/{id}/activate")
        public ResponseEntity<PaymentMethodDTO> activatePaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO) {
            PaymentMethod paymentMethod = paymentMethodService.setMethodAvailability(paymentMethodDTO.getPaymentMethodId(), true);
            return ResponseEntity.ok(new PaymentMethodDTO(paymentMethod));
        }

        @PutMapping("/{id}/deactivate")
        public ResponseEntity<PaymentMethodDTO> deactivatePaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO) {
            PaymentMethod paymentMethod = paymentMethodService.setMethodAvailability(paymentMethodDTO.getPaymentMethodId(), false);
            return ResponseEntity.ok(new PaymentMethodDTO(paymentMethod));
        }
    }
