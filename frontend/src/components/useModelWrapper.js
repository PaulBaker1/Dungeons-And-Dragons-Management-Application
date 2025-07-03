import { computed } from 'vue'

export function useModelWrapper(props, emit) {
    const model = computed({
        get: () => props.modelValue,
        set: (value) => emit('update:modelValue', value)
    })

    return { model }
}
