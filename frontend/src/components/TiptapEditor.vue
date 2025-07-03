<script setup>
import { onBeforeUnmount, watch, ref } from 'vue'
import { EditorContent, useEditor } from '@tiptap/vue-3'
import StarterKit from '@tiptap/starter-kit'
import Heading from '@tiptap/extension-heading'
import BulletList from '@tiptap/extension-bullet-list'
import OrderedList from '@tiptap/extension-ordered-list'
import ListItem from '@tiptap/extension-list-item'
import Strike from '@tiptap/extension-strike'
import TextStyle from '@tiptap/extension-text-style'
import FontSize from 'tiptap-extension-font-size'



const props = defineProps({
  modelValue: { type: String, required: true }
})
const emit = defineEmits(['update:modelValue'])

const editor = useEditor({
  content: props.modelValue,
  extensions: [
    StarterKit.configure({
      heading: false,
      textStyle: false
    }),
    Heading.configure({ levels: [1, 2, 3] }),
    TextStyle,
    FontSize.configure({ types: ['textStyle'] }),
    Strike,
  ],
  editorProps: {
    attributes: {
      class: 'focus:outline-none prose prose-invert',
    },
  },
  onUpdate: ({ editor }) => emit('update:modelValue', editor.getHTML()),
})


watch(() => props.modelValue, (value) => {
  if (editor.value && value !== editor.value.getHTML()) {
    editor.value.commands.setContent(value, false)
  }
})

onBeforeUnmount(() => editor.value?.destroy())

const fontSizes = ['12px', '14px', '16px', '18px', '20px', '24px', '32px']
const applyFontSize = (size) => {
  editor.value?.chain().focus().setFontSize(size).run()
}

const btnClass = (name, opts = {}) => [
  'px-2 py-1 rounded text-sm',
  editor.value?.isActive(name, opts)
      ? 'bg-green-500 text-white'
      : 'bg-slate-700 text-slate-200'
]

const toggle = (name, opts = {}) => {
  if (!editor.value) return
  const chain = editor.value.chain().focus()
  switch (name) {
    case 'bold': chain.toggleBold().run(); break;
    case 'italic': chain.toggleItalic().run(); break;
    case 'strike': chain.toggleStrike().run(); break;
    case 'heading1': chain.toggleHeading({ level: 1 }).run(); break;
    case 'heading2': chain.toggleHeading({ level: 2 }).run(); break;
    case 'heading3': chain.toggleHeading({ level: 3 }).run(); break;
  }
}
</script>

<template>
  <div class="space-y-2">
    <!-- Toolbar -->
    <div class="flex flex-wrap gap-2 bg-slate-800 p-2 rounded border border-slate-700">
      <button @click="toggle('bold')" :class="btnClass('bold')">B</button>
      <button @click="toggle('italic')" :class="btnClass('italic')"><em>I</em></button>
      <button @click="toggle('strike')" :class="btnClass('strike')"><s>S</s></button>
      <button @click="toggle('heading1')" :class="btnClass('heading', { level: 1 })">Title</button>
      <button @click="toggle('heading2')" :class="btnClass('heading', { level: 2 })">Heading</button>
      <button @click="toggle('heading3')" :class="btnClass('heading', { level: 3 })">Subheading</button>

      <!-- 🔤 Font size -->
      <select @change="applyFontSize($event.target.value)" class="bg-slate-700 text-white px-2 py-1 rounded text-sm border border-slate-600">
        <option disabled selected>Font Size</option>
        <option v-for="size in fontSizes" :key="size" :value="size">{{ size }}</option>
      </select>
    </div>

    <!-- Editor -->
    <div class="border border-slate-700 bg-slate-900 text-white rounded p-4 min-h-[150px]">
      <EditorContent :editor="editor" />
    </div>
  </div>
</template>

<style scoped>
ul, ol {
  padding-left: 1.5rem;
  margin-top: 0.5rem;
  margin-bottom: 0.5rem;
}

ul { list-style-type: disc; }
ol { list-style-type: decimal; }

button {
  transition: background-color 0.2s;
}

select {
  cursor: pointer;
}
</style>
