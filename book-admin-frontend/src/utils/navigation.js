// utils/navigation.js
import { useRouter, useRoute } from 'vue-router'

export function useNavigation() {
  const router = useRouter()
  const route = useRoute()

  function pushBookDetail(bookId) {
    router.push({
      path: `/book/bookDetail/${bookId}`,
      query: {
        from: route.fullPath,
      },
    })
  }

  return {
    pushBookDetail,
  }
}
